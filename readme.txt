1. unzip and place the file in any folder like 'P:\crawler'
2. got 'P:\crawler' in cammand prompt
3. Build using maven ->'mvn clean install'
4. After maven build, run -> 'mvn -Dmaven.tomcat.port=8085 tomcat:run-war', this will start tomcat server.
5. Go to browser and use URL http://localhost:8085/crawler/index.xhtml. this will load the page with site mapping

