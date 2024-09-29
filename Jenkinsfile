pipeline {
    agent any
       parameters {
        choice(choices: ['silence' , 'greeting'], description: '',name: 'REQUESTED_ACTION')
        choice(choices: getData2(), description: '',name: 'REQUESTED_ACTION2')
        choice(choices: getData1(), description: '',name: 'REQUESTED_ACTION2')
        
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
agent {
   // checkout scm 
    def rootDir = pwd()
    utilModule  = load "${rootDir}/jenkins/util.Groovy"
    utilModule.printHello()
}

def getData2() {
		List devList  = ["Select:selected", "dev1", "dev2"]
   return devList
}

def getData1() {
	def utilModule  
    agent
    {
		
    utilModule  = load "${env.WORKSPACE}/jenkins/util.Groovy"
    println("${env.WORKSPACE}/jenkins/util.Groovy")
    utilModule.printHello()
    utilModule.getData()	
    }
		
   return ["dev", "prod"]
}

