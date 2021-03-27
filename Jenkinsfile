pipeline {
    agent any
	tools {
	    maven 'mvn3'  
	    jdk 'jdk15'  
	}
	
	options {
	    buildDiscarder(logRotator(numToKeepStr: '5'))
	    disableConcurrentBuilds()
	}
	
    stages {
        stage('Build & Deploy SNAPSHOT') {
           steps {
              deploy()
           }
           post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        } 

        stage('Release') {
            when {
                anyOf {               
                    branch 'master';
                    branch 'netty-client'
                }
            }
            steps {
                script {
                    version = release()
                    echo "Released binance-java-api version ${version}"
                    currentBuild.displayName = version
                }
            }
        }
    }
}