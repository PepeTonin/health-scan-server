@echo off
cls
set /p escolha="Você deseja baixar o arquivo csv da fonte? (s/n)"

if /i "%escolha%"=="s" (
    powershell -Command Invoke-WebRequest -Uri "https://static.openfoodfacts.org/data/en.openfoodfacts.org.products.csv" -OutFile "entrada.csv"
)

python generate.py
python insert_foodsfacts_data.py

pause
