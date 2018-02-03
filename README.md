# SimplePayUMoneyApp
This is very simple implementation of PayUMoney Payment Gateway. This project is developed for first time user of PayUMoney.
This project contains very basics of PayUMoney Payment Gateway and saves your time by avoiding "Some Error Occurred" error.

 Add APP LEVEL dependancies :<br />
   `compile('com.payumoney.sdkui:plug-n-play:1.0.0'){
        transitive = true;
        exclude module: 'payumoney-sdk'
    }
    compile 'com.payumoney.core:payumoney-sdk:7.0.1'`
    
Get Hash Generation script from [Hash Genererator](https://gist.github.com/mayur19/26417eeba02f5e5ced8138f4d9040d6a)

Please find below test credentials.<br />MID : 4934580<br />Key : rjQUPktU<br />Salt : e5iIg1jwi8


Below is the test card details for doing a test transaction in the testing mode.<br />
Card No - 5123456789012346<br />
Expiry - 05/2020<br />
CVV - 123<br />

**Important Note: Test Credentials from PayUMoney account section are not working. 
This application uses universal credentials which is given by PayUMoney. For testing you can use any furl & surl URL.**
