@echo off
echo ========================================
echo   Compilando Proyecto Funds BTG
echo ========================================
echo.

echo [1/4] Limpiando proyecto...
call mvn clean

echo.
echo [2/4] Descargando dependencias...
call mvn dependency:resolve

echo.
echo [3/4] Compilando codigo fuente...
call mvn compile

echo.
echo [4/4] Ejecutando tests...
call mvn test

echo.
echo ========================================
echo   Compilacion finalizada
echo ========================================
pause

