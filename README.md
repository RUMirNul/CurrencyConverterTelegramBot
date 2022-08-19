# CurrencyConverterTelegramBot

### Introduction
Telegram bot for currency conversion at the current exchange rate. The bot supports 160+ currencies, it can convert to several currencies at once and any amount.

### Using 
In src/main/resources/application.yml set the configuration parameters:
```
telegram:    
  bot:    
    name: *** - Telegram bot name    
    token: *** - Telegram bot token    
    webhookPath: *** - Webhook for communicate with the Telegram bot     

currency:     
  api:     
    allPath: https://api.getgeoapi.com/v2/currency/list?api_key=APITOKEN - APITOKEN replace on your API Token from getgeoapi.com   
    convertPath: https://api.getgeoapi.com/v2/currency/convert?api_key=APITOKEN&from={from}&to={to}&amount={amount}&format=json - APITOKEN replace on your API Token from getgeoapi.com  
```
  
