pipeline {
    agent any
       parameters {
        choice(
            choices: ['silence' , 'greeting'],
            description: '',
            name: 'REQUESTED_ACTION')
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B clean package' 
            }
        }
        stage('Report') {
            steps {
                junit '**/surefire-reports/*.xml' 
            }			
		}
        stage('Report2') {
            when {
                expression { params.REQUESTED_ACTION == 'greeting' }
            }
            steps {
                echo "Hello, greeting!"
            }			
		}		
    }
}

def utilModule
node {
    checkout scm 
    utilModule = load "jenkins/util.Groovy"
}
utilModule.printHello()

