properties([
   parameters([
	activeChoice(
        choiceType: 'PT_SINGLE_SELECT',
        filterLength: 1,
        filterable: false,
        name: 'Env',
        script: groovyScript(
            fallbackScript: [
                classpath: [],
                oldScript: '',
                sandbox: true,
                script: "return ['Could not get the environments']"
            ],
            script: [
                classpath: [],
                oldScript: '',
                sandbox: true,
                script: "return ['dev', 'stage', 'prod']"
            ]
        )
          ),
	      [
         $class: 'ChoiceParameter',
         choiceType: 'PT_SINGLE_SELECT',
         description: "Select which type",
         filterLength: 1,
         filterable: false, 
         name: 'TYPE', 
         script: [
            $class: 'GroovyScript', 
            fallbackScript: [
               classpath: [], 
               sandbox: true,
               script: "return['']"
            ], 
            script: [
               classpath: [], 
               sandbox: true,
               script: "return ['TYPE1', 'TYPE2', 'TYPE3', 'TYPE4']"
            ]
         ]
      ],
      [
         $class: 'CascadeChoiceParameter',
         choiceType: 'PT_SINGLE_SELECT', 
         description: 'Select which sub type',
         name: 'SUB_TYPE', 
         referencedParameters: 'TYPE',
         script: [
            $class: 'GroovyScript', 
            fallbackScript: [
               classpath: [], 
               sandbox: true, 
               script: "return['Select which type']"
            ], 
            script: [
               classpath: [], 
               sandbox: true,
               script:  "return['Select which type']"
            ] 
         ]
      ]
   ])
])
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
   // checkout scm 
    def rootDir = pwd()
    utilModule  = load "${rootDir}/jenkins/util.Groovy"
    utilModule.printHello()
}

def getData2() {
		List devList  = ["Select:selected", "dev1", "dev2"]
   return devList
}