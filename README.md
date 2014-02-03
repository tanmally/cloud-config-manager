cloud-config-manager
====================

Configuration manager that can be accessed centrally. Control all application properties over the web in a secure way .
We can modify the application/project property and view the impact real time . This setup will be helpful if we run multiple instance of our app in cloud and we need elastic scaling , in this scenario we can modify the app properties centrally and all running instance would pick up the app properties from the cloud config manager without restarting the app.

The basic structure of application/project property

      Project
            Environment
                      property 1
                      property 2
                      property 3
            
            
Example structure

      Project - bank-app
            Environmant - dev 
                      property 1 - db url
                      property 2 - db username
                      property 3 - db password
            Environment - prod
                      property 1 - db url
                      property 2 - db username
                      property 3 - db password
                      
Note : At any point of time we can have only one environment as active and only those properties will be used by the application/project . In the above case when you set  'dev' as active then  'db url' given in dev  environment will be returned.


  Cloud Config Server - [Getting started](https://github.com/bbytes/cloud-config-manager/tree/master/cloud-config-manager-server)

  Cloud Config Client - [Getting started](https://github.com/bbytes/cloud-config-manager/tree/master/cloud-config-manager-client)


