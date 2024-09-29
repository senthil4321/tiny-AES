pipeline {
    agent any
    stages {
        stage('Build1') { 
            steps {
                sh 'mvn -B clean package' 
            }
        }
        stage('Report') {
            steps {
                junit '**/surefire-reports/*.xml' 
                
            }			
		}
    }
}
