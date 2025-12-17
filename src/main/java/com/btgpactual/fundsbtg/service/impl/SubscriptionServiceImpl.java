package com.btgpactual.fundsbtg.service.impl;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.dto.request.SubscriptionRequest;
import com.btgpactual.fundsbtg.dto.response.SubscriptionResponse;
import com.btgpactual.fundsbtg.exception.DuplicateSubscriptionException;
import com.btgpactual.fundsbtg.exception.InsufficientBalanceException;
import com.btgpactual.fundsbtg.exception.ResourceNotFoundException;
import com.btgpactual.fundsbtg.mapper.SubscriptionMapper;
import com.btgpactual.fundsbtg.model.Fund;
import com.btgpactual.fundsbtg.model.Subscription;
import com.btgpactual.fundsbtg.model.User;
import com.btgpactual.fundsbtg.repository.FundRepository;
import com.btgpactual.fundsbtg.repository.SubscriptionRepository;
import com.btgpactual.fundsbtg.repository.UserRepository;
import com.btgpactual.fundsbtg.service.NotificationService;
import com.btgpactual.fundsbtg.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final FundRepository fundRepository;
    private final NotificationService notificationService;
    
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                  UserRepository userRepository,
                                  FundRepository fundRepository,
                                  NotificationService notificationService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.fundRepository = fundRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    @Transactional
    public Mono<SubscriptionResponse> createSubscription(SubscriptionRequest request) {
        logger.info("Creando suscripción - Usuario: {}, Fondo: {}", request.getUserId(), request.getFundId());
        
        return validateNoActiveSubscription(request.getUserId(), request.getFundId())
                .then(Mono.zip(
                        userRepository.findById(request.getUserId())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException(AppConstants.MSG_USER_NOT_FOUND))),
                        fundRepository.findById(request.getFundId())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException(AppConstants.MSG_FUND_NOT_FOUND)))
                ))
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    Fund fund = tuple.getT2();
                    
                    if (!user.hasSufficientBalance(fund.getMinimumInvestmentAmount())) {
                        logger.warn("Saldo insuficiente - Usuario: {}, Saldo: {}, Requerido: {}", 
                                  user.getId(), user.getBalance(), fund.getMinimumInvestmentAmount());
                        return Mono.error(InsufficientBalanceException.forFund(
                                fund.getPrettyName(), 
                                user.getBalance(), 
                                fund.getMinimumInvestmentAmount()));
                    }
                    
                    user.deductBalance(fund.getMinimumInvestmentAmount());
                    
                    Subscription subscription = Subscription.builder()
                            .userId(user.getId())
                            .fundId(fund.getId())
                            .fundName(fund.getName())
                            .fundPrettyName(fund.getPrettyName())
                            .investmentAmount(fund.getMinimumInvestmentAmount())
                            .status(true)
                            .build();
                    
                    return userRepository.save(user)
                            .then(subscriptionRepository.save(subscription))
                            .flatMap(savedSubscription -> {
                                logger.info("Suscripción creada exitosamente con ID: {}", savedSubscription.getId());
                                
                                notificationService.sendSubscriptionNotification(user, fund, savedSubscription)
                                        .subscribe(
                                                result -> logger.info("Notificación enviada exitosamente"),
                                                error -> logger.error("Error al enviar notificación: {}", error.getMessage())
                                        );
                                
                                return Mono.just(SubscriptionMapper.toResponse(savedSubscription));
                            });
                })
                .doOnError(error -> logger.error("Error al crear suscripción: {}", error.getMessage()));
    }
    
    @Override
    @Transactional
    public Mono<SubscriptionResponse> cancelSubscription(SubscriptionRequest request) {
        logger.info("Cancelando suscripción - Usuario: {}, Fondo: {}", request.getUserId(), request.getFundId());
        
        return subscriptionRepository.findByUserIdAndFundIdAndStatus(
                        request.getUserId(), 
                        request.getFundId(), 
                        true)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(AppConstants.MSG_SUBSCRIPTION_NOT_FOUND)))
                .zipWhen(subscription -> userRepository.findById(subscription.getUserId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException(AppConstants.MSG_USER_NOT_FOUND))))
                .flatMap(tuple -> {
                    Subscription subscription = tuple.getT1();
                    User user = tuple.getT2();
                    
                    user.addBalance(subscription.getInvestmentAmount());
                    subscription.cancel(request.getCancellationReason());
                    
                    return userRepository.save(user)
                            .then(subscriptionRepository.save(subscription))
                            .flatMap(savedSubscription -> {
                                logger.info("Suscripción cancelada exitosamente - ID: {}", savedSubscription.getId());
                                
                                fundRepository.findById(subscription.getFundId())
                                        .flatMap(fund -> notificationService.sendCancellationNotification(user, fund, savedSubscription))
                                        .subscribe(
                                                result -> logger.info("Notificación de cancelación enviada"),
                                                error -> logger.error("Error al enviar notificación de cancelación: {}", error.getMessage())
                                        );
                                
                                return Mono.just(SubscriptionMapper.toResponse(savedSubscription));
                            });
                })
                .doOnError(error -> logger.error("Error al cancelar suscripción: {}", error.getMessage()));
    }
    
    @Override
    public Flux<SubscriptionResponse> getActiveSubscriptions(String userId) {
        logger.info("Obteniendo suscripciones activas para usuario: {}", userId);
        
        return subscriptionRepository.findByUserIdAndStatus(userId, true)
                .map(SubscriptionMapper::toResponse)
                .doOnComplete(() -> logger.info("Suscripciones activas recuperadas para usuario: {}", userId))
                .doOnError(error -> logger.error("Error al obtener suscripciones activas: {}", error.getMessage()));
    }
    
    @Override
    public Flux<SubscriptionResponse> getSubscriptionHistory(String userId) {
        logger.info("Obteniendo historial de suscripciones para usuario: {}", userId);
        
        return subscriptionRepository.findByUserId(userId)
                .map(SubscriptionMapper::toResponse)
                .doOnComplete(() -> logger.info("Historial de suscripciones recuperado para usuario: {}", userId))
                .doOnError(error -> logger.error("Error al obtener historial de suscripciones: {}", error.getMessage()));
    }
    
    @Override
    public Mono<SubscriptionResponse> getSubscriptionById(String subscriptionId) {
        logger.info("Buscando suscripción con ID: {}", subscriptionId);
        
        return subscriptionRepository.findById(subscriptionId)
                .map(SubscriptionMapper::toResponse)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Suscripción no encontrada con ID: {}", subscriptionId);
                    return Mono.error(new ResourceNotFoundException(AppConstants.MSG_SUBSCRIPTION_NOT_FOUND));
                }));
    }
    
    private Mono<Void> validateNoActiveSubscription(String userId, String fundId) {
        return subscriptionRepository.existsByUserIdAndFundIdAndStatus(userId, fundId, true)
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        logger.warn("Intento de suscripción duplicada - Usuario: {}, Fondo: {}", userId, fundId);
                        return Mono.error(new DuplicateSubscriptionException(
                                AppConstants.MSG_SUBSCRIPTION_EXISTS));
                    }
                    return Mono.empty();
                });
    }
}
