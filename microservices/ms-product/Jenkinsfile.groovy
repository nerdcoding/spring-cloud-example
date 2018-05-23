#!groovy

pipeline {
    agent any

    tools {
        maven 'maven-3.5.3'
        jdk 'jdk-10.0.1'
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout(scm)
            }
        }
        stage("Create version number") {
            steps {
                script {
                    def pom1 = readMavenPom file: 'pom.xml'
                    newversion = pom1.version
                    newversion = newversion.replaceAll('SNAPSHOT', '')
                    newversion = newversion + Calendar.getInstance().getTime().format('YYYYMMdd-HHmmss',TimeZone.getTimeZone('UTC'))
                }
                sh "mvn -B -N -e versions:set -DgenerateBackupPoms=false -DnewVersion=${newversion}"
            }
        }

        stage('Compile & Test') {
            steps {
                sh "mvn -B -V -U -e clean package -Dsurefire.useFile=false"
            }
        }

        stage('Release') {
            steps {
                script {
                    sh "git config --global user.email \"jenkins@example.com\""
                    sh "git config --global user.name \"Jenkins Build\""
                    sh "git add ."
                    sh "git commit -a -m \"Version ${newversion}\""
                    sh "git tag -a -m \"Tag ${newversion}\" ${newversion}"
                    sh "git push origin --tags"
                }
            }
        }

    }
}