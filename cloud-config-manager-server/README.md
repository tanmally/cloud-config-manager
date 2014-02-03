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
    
    

## REST APIs   

To return all projects in cloud config manager

    GET /project/list

To list all environments for given project 

    GET /project/{project}/environment
    
To return the project property list based on given environment 

    GET /project/{project}/{environment}/property

To return the project property list based on active environment 

    GET /project/{project}/property

