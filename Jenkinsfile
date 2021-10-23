
node() {
    def jenkinsCredentialsId = 'MyGitHub'
    def commonBranchName = ''

    //First, check out the application project
    dir('Sample') {
        checkout scm
	git url: 'https://github.com/FrontlineEducation/android-Common.git', credentialsId: jenkinsCredentialsId, branch: 'develop'
    }

    dir('Sample'){
        def currentStage = "NOT STARTED";

        try {

            stage('Build') {
                currentStage = 'Build - Refresh and Clean';
         
                    sh './gradlew --refresh-dependencies clean assembleRelease'
     
            }


         //   stage('Sign'){
           //     currentStage = 'Sign';
             //   if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'MOB-6109') {
               //     signAndroidApks keyStoreId: "AndroidBuildKey", keyAlias: "", apksToSign: "**/*-unsigned.apk"
                //}
           // }

       //       stage('Sign'){
         //     currentStage = 'Sign';
           //       withCredentials([string(credentialsId: 'keystorepass', variable: 'KEY_PASSWORD'),file(credentialsId: 'keystorefile', variable: 'KEYSTORE')])
             //     {
               //    sh '/c/Users/global/AppData/Local/Android/Sdk/build-tools/29.0.3/zipalign -v -p 4 /C/CustomJenkinsSpace/Jenkins/.jenkins/workspace/QAPipeline_MOB-6109/android-FrontlineMobile/app/build/outputs/apk/stage/app-*-unsigned.apk /C/CustomJenkinsSpace/Jenkins/.jenkins/workspace/QAPipeline_MOB-6109/android-FrontlineMobile/app/build/outputs/apk/stage/unsigned-aligned.apk'
                 //  sh '/c/Users/global/AppData/Local/Android/Sdk/build-tools/29.0.3/apksigner.bat sign --ks $KEYSTORE --ks-pass pass:$KEY_PASSWORD --out /D/MyJenkinsApks/FLMobile/stage/fl_signed.apk /C/CustomJenkinsSpace/Jenkins/.jenkins/workspace/QAPipeline_MOB-6109/android-FrontlineMobile/app/build/outputs/apk/stage/unsigned-aligned.apk'
                   //}
               // currentBuild.result = 'SUCCESS'
             //}

           
              stage("Deployment"){
              if(env.result == 'SUCCESS'){
              sh 'echo "Started with the Deployment process"'
              }
              }
        }
        catch(error) {
            currentBuild.result = 'FAILURE'
            throw new hudson.AbortException(error.toString());
        }
    }
}
