#!/usr/bin/env groovy
@Library('common-library')

//定义包ID, name和路径
def APPS = [
	['ops-web-erp','ops-web-erp','target/ops-web-erp-*.war']
]

//定义构建的参数
def buildMapParams=[:]
//构建pom所在目录
buildMapParams.pomDir='.'

pipeline {
    agent any
    tools {
        maven 'mvn3'
        jdk 'jdk8'
    }
	
    stages {
       stage('构建') {
	        steps {
			    commonBuild(buildMapParams)
			}   
    	}
    	stage('打包和上传') {
    		steps{
    			packageAndUpload(APPS)
    		}    		
        }
    }
}
