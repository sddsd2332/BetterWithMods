#!/usr/bin/env groovy

pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Setup') {
            steps {
                echo 'Setting up Workspace'
                sh './gradlew setupCiWorkspace'
            }
        }
        stage('Build and Deploy') {
            steps {
                echo 'Building and Deploying to Maven'
                script {
                    sh './gradlew build uploadArchives'
                }
            }
        }
    }
    post {
        always {
            archive 'build/libs/**.jar'
            discordSend description: 'Jenkins Pipeline Build', footer: 'Footer Text', link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: env.BWM_DISCORD
        }
    }
}