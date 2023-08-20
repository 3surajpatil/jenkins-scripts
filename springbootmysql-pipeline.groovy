#!/groovy


node {
   
    //def server = 
    // Create an Artifactory Gradle instance.
    //def rtGradle = Artifactory.newGradleBuild()
    //def buildInfo

    stage('build') {
        echo "build started"
        sh "whoami"
        sh 'cp -r /home/suraj/workload/git/spring-boot-apps/springbootmysql/* .'         
        sh 'mvn clean install -DskipTests'          
    }

    stage('deploy') {
         echo 'stopping old container'
                        sh 'docker ps -f name=suraj-sb-mysql -q | xargs --no-run-if-empty docker container stop'

                        echo 'removing old stopped container'
                        sh 'docker container ls -a -f name=suraj-sb-mysql -q | xargs -r docker container rm'

                        echo 'removing image'
                        sh 'docker rmi -f local:springboot-mysql'

                        echo 'building docker image'
                        sh 'docker build -t local:springboot-mysql .'
                        sh 'docker run -d --network host --name suraj-sb-mysql --restart unless-stopped local:springboot-mysql'
 
    }

}

