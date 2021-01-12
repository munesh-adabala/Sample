
node() {
    def jenkinsCredentialsId = 'bf435957-b899-4019-8cd0-4e724a3cfcf9'
    def commonBranchName = ''

    //First, check out the application project
    dir('android-FrontlineMobile') {
        checkout scm

        //Now we need to know if env.BRANCH_NAME is actually a branch name or a PR number. We use the hub command to find out
        //if BRANCH_NAME is a known PR. If it is, we ask hub to give us the corresponding branch name, otherwise, we treat
        //BRANCH_NAME as if it really is the name of the branch.

        withEnv(['PATH+EXTRA=/usr/local/bin']) {
            commonBranchName = sh(returnStdout: true, script:'./getCommonBranchName.sh ${BRANCH_NAME}')
        }
    }

    dir('android-Common') {
        //If a branch exists on the library project with the same name as the branch of the application project, then checkout that branch
        //Otherwise, checkout either develop or master, depending on env.BRANCH_NAME

        withCredentials([usernamePassword(credentialsId: jenkinsCredentialsId, usernameVariable: 'GIT_U', passwordVariable: 'GIT_P')]) {
            def command = 'git ls-remote --heads --exit-code https://${GIT_U}:${GIT_P}@github.com/FrontlineEducation/android-Common.git ' + commonBranchName

            //status of 2 tells us that no branch exists in the repo matching the specified branch name
            def status = sh(returnStatus: true, script: command)

            if (status == 2 || commonBranchName == 'master' || commonBranchName == 'unknown') {
                if (env.BRANCH_NAME == 'master') {
                    git url: 'https://github.com/FrontlineEducation/android-Common.git', credentialsId: jenkinsCredentialsId
                }
                else {
                    git url: 'https://github.com/FrontlineEducation/android-Common.git', credentialsId: jenkinsCredentialsId, branch: 'develop'
                }
            }
            else {
                git url: 'https://github.com/FrontlineEducation/android-Common.git', credentialsId: jenkinsCredentialsId, branch: commonBranchName
            }
        }
    }

    dir('android-FrontlineMobile') {
        sh 'git log --format="%ae" | head -1 | cut -d@ -f1 > commit-author.txt'
        def masterOfDisaster = readFile('commit-author.txt').trim()
        def currentStage = "NOT STARTED";

        try {
            stage('Source Validation') {
                sh './validateSource.sh'
            }

            stage('Build') {
                currentStage = 'Build - Refresh and Clean';

                if (env.BRANCH_NAME == 'develop') {
                    sh './gradlew --refresh-dependencies clean assembleAuto assembleStage'
                }
                else {
                    sh './gradlew --refresh-dependencies clean assembleAuto assembleRelease'
                }

                currentStage = 'Build - Jacoco Test Report';

                withEnv(["ANDROID_SERIAL=emulator-5554"]) {
                    lock('emulatorAndroid') {
                        sh './gradlew :app:jacocoTestReport'
                    }
                }
            }

            stage('Gradle Static Analysis') {
                currentStage = 'Gradle Static Analysis';
                withSonarQubeEnv {
                    sh "./gradlew --info :app:sonarqube"
                }
            }

            stage('Quality Gate') {
                currentStage = 'Quality Gate';
                def qg = waitForQualityGate()
                if( qg.status != "OK" ){
                    sh 'exit 1'
                }
            }

            stage('Sign'){
                currentStage = 'Sign';
                if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'develop') {
                    signAndroidApks keyStoreId: "AndroidBuildKey", keyAlias: "", apksToSign: "**/*-unsigned.apk"
                }
            }

            stage('Upload to Google Play Store'){
                currentStage = 'Upload to Google Play Store';

                //When enabling Proguard with MOB-1238, also add below:  deobfuscationFilesPattern: '**/mapping.txt',

                //Quick Guide to how we will use Google Play app publishing tracks:
                //Internal: every build from the develop branch will be published here (these are not release candidates because they point at Stage)
                //Alpha: every build from the master branch will be published here (these are release candidates because they point at Prod)
                //Beta: intended for open/public betas (not likely to be used often, if at all)

                if (env.BRANCH_NAME == 'develop') {
                    androidApkUpload googleCredentialsId: 'Google Play', apkFilesPattern: '**/app-stage.apk', trackName: 'internal'
                }

                if (env.BRANCH_NAME == 'master') {
                    androidApkUpload googleCredentialsId: 'Google Play', apkFilesPattern: '**/app-release.apk', trackName: 'alpha'
                }
            }

            stage('Install on physical device'){
                currentStage = 'Install on physical device';
                //sh '/Users/mobiledevteam/Library/Android/sdk/platform-tools/adb -d install **/app-release-unsigned.apk'
            }

            currentBuild.result = 'SUCCESS'
        }
        catch(error) {

            slackSend channel: '#tm-mobile-builds', color: 'danger', message: ":android: :frontline: ${env.BRANCH_NAME}  @${masterOfDisaster} broke the build!  Failed during ${currentStage} "
            currentBuild.result = 'FAILURE'
            throw new hudson.AbortException(error.toString());
        }
    }
}
