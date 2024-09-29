pipeline {
    agent any
       parameters {
        choice(
            choices: ['silence' , 'greeting'],
            description: '',
            name: 'REQUESTED_ACTION')
        activeChoiceParam('CHOICE-1') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script: "$getData2"
            }
            }            
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
    def rootDir = pwd()
    utilModule  = load "${rootDir}/jenkins/util.Groovy"
    utilModule.printHello()
}

def getData2(JOB_NAME) {
		List devList  = ["Select:selected", "dev1", "dev2"]
   return devList
}