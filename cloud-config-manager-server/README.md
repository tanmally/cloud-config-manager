Cloud config server usage
====================

Start the server :

    java -jar cloud-config-manager-server-1.0.0.jar
    
When you start the server you will see a msg printed on console :   

    Cloud Config Server starting on port: 9000
    
The default port is 9000 but if you want to start with a specific port no  

    java -jar cloud-config-manager-server-1.0.0.jar --port=9001

Finally once the server is started go the browser and try 

    http://localhost:9000/ping
    
You will see the below msg in browser 

    Cloud Config manager, it works !!
