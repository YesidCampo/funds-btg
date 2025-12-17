package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.dto.request.SubscriptionRequest;
import com.btgpactual.fundsbtg.dto.response.SubscriptionResponse;
import com.btgpactual.fundsbtg.exception.DuplicateSubscriptionException;
import com.btgpactual.fundsbtg.exception.InsufficientBalanceException;
import com.btgpactual.fundsbtg.exception.ResourceNotFoundException;
import com.btgpactual.fundsbtg.model.Fund;
import com.btgpactual.fundsbtg.model.Subscription;
import com.btgpactual.fundsbtg.model.User;
import com.btgpactual.fundsbtg.repository.FundRepository;
import com.btgpactual.fundsbtg.repository.SubscriptionRepository;
import com.btgpactual.fundsbtg.repository.UserRepository;
import com.btgpactual.fundsbtg.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para SubscriptionServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {
    
    @Mock
    private SubscriptionRepository subscriptionRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private FundRepository fundRepository;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;
    
    private SubscriptionRequest subscriptionRequest;
    private User user;
    private Fund fund;
    private Subscription subscription;
    
    @BeforeEach
    void setUp() {
        subscriptionRequest = SubscriptionRequest.builder()
                .userId("user1")
                .fundId("fund1")
                .build();
        
        user = User.builder()
                .id("user1")
                .userName("Juan Pérez")
                .email("juan@example.com")
                .balance(new BigDecimal("500000"))
                .build();
        
        fund = Fund.builder()
                .id("fund1")
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .prettyName("BTG Pactual Recaudadora")
                .minimumInvestmentAmount(new BigDecimal("75000"))
                .currency("COP")
                .category(FundCategory.FPV)
                .build();
        
        subscription = Subscription.builder()
                .id("sub1")
                .userId("user1")
                .fundId("fund1")
                .fundName(fund.getName())
                .fundPrettyName(fund.getPrettyName())
                .investmentAmount(fund.getMinimumInvestmentAmount())
                .status(true)
                .build();
    }
    
    @Test
    void createSubscription_Success() {
        // Given
        when(subscriptionRepository.existsByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.just(false));
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));
        when(fundRepository.findById(anyString())).thenReturn(Mono.just(fund));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(Mono.just(subscription));
        when(notificationService.sendSubscriptionNotification(any(), any(), any()))
                .thenReturn(Mono.empty());
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.createSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getUserId().equals("user1") &&
                        response.getFundId().equals("fund1") &&
                        response.getStatus())
                .verifyComplete();
        
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void createSubscription_DuplicateSubscription() {
        // Given
        when(subscriptionRepository.existsByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.just(true));
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
        when(fundRepository.findById(anyString())).thenReturn(Mono.empty());
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.createSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(DuplicateSubscriptionException.class)
                .verify();
        
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }
    
    @Test
    void createSubscription_InsufficientBalance() {
        // Given
        User poorUser = User.builder()
                .id("user1")
                .userName("Juan Pérez")
                .balance(new BigDecimal("50000")) // Menos del mínimo requerido
                .build();
        
        when(subscriptionRepository.existsByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.just(false));
        when(userRepository.findById(anyString())).thenReturn(Mono.just(poorUser));
        when(fundRepository.findById(anyString())).thenReturn(Mono.just(fund));
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.createSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(InsufficientBalanceException.class)
                .verify();
        
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }
    
    @Test
    void createSubscription_UserNotFound() {
        // Given
        when(subscriptionRepository.existsByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.just(false));
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
        when(fundRepository.findById(anyString())).thenReturn(Mono.just(fund));
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.createSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
    
    @Test
    void cancelSubscription_Success() {
        // Given
        when(subscriptionRepository.findByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.just(subscription));
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(Mono.just(subscription));
        when(fundRepository.findById(anyString())).thenReturn(Mono.just(fund));
        when(notificationService.sendCancellationNotification(any(), any(), any()))
                .thenReturn(Mono.empty());
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.cancelSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getUserId().equals("user1") &&
                        response.getFundId().equals("fund1"))
                .verifyComplete();
        
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void cancelSubscription_NotFound() {
        // Given
        when(subscriptionRepository.findByUserIdAndFundIdAndStatus(anyString(), anyString(), any()))
                .thenReturn(Mono.empty());
        
        // When
        Mono<SubscriptionResponse> result = subscriptionService.cancelSubscription(subscriptionRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
        
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }
}

