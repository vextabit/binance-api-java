pipeline {
    agent any
	tools {
	    maven 'mvn3'  
	    jdk 'jdk14'  
	}
	
	options {
	    buildDiscarder(logRotator(numToKeepStr: '5'))
	    disableConcurrentBuilds()
	}
	
	triggers {
	    pollSCM('')
	}
	
    parameters {
        booleanParam(
            name: "RELEASE",
            description: "Build a release from current commit.",
            defaultValue: false)
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
                branch 'master'
            }
            steps {
                script {                    
                    version = release()
                    echo "Released binance-java-api version ${version}"
                }
            }
        }
    }
}