pipeline {
    agent any
    stages {
        stage('Build1') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
         stage('Test') { 
            steps {
                sh 'mvn test' 
            }
        }
    }
}
