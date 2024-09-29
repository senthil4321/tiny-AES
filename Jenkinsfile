pipeline {
    agent any
       parameters {
        choice(choices: ['silence' , 'greeting'], description: '',name: 'REQUESTED_ACTION')
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
def getData1() {
	def utilModule1
	node('LOCAL') 
	{  
    utilModule1  = load "${env.WORKSPACE}//jenkins//util.Groovy"
    utilModule1.printHello()
    }
   return  utilModule1.getData()
}

