pipeline {
	agent any
	environment {
		APP_DIR="~/app"
		JAR_NAME="SpringRecipeAIProject-0.0.1-SNAPSHOT.jar"
	}
	
	stages {
		/*
			git push = commit (main)
				|
			web hook
				|
			Jenkins (local) -> 나중에 EC2로 바뀜
				|
			  build
			  	|
			docker build
				|
			docker push
				|
			docker pull
				|
			docker run
		*/
		stage('check out') { // 소스파일 클론 하는 것
			steps {
				echo 'Git Checkout' 
				checkout scm 
			}
		}
		
		state('Create .env') {
			steps {
				withCredentials([
					string(
						credentialsid: 'post-url'
						variable: 'POST_URL'
					)
					,string(
						credentialsid: 'gen-key'
						variable: 'GEN-KEY'
					)
				]){
					sh '''
						cat > .env << EOF
						SPRING_PROFILES_ACTIVE=prod
						POST_URL=${POST_URL}
						GEN_KEY=${GEN_KEY}
						EOF
							chmod 600 .env
					   '''
				}
			}
		}
		
		stage('Gradlew Permission') {
			steps {
				sh '''
					chmod +x gradlew
				   '''
			}
		}
		
		stage('Gradle build') {
			steps {
				sh '''
					./gradlew clean build -x test
				   '''
			}
		}
		
		stage('Docker build') {
			steps {
				sh '''
					docker build -t jeunju528/ai-app:lastet .
				   '''
			}
		}
		
		stage('Docker Hub Login') {
			steps {
				withCredentials([usernamePassword(
					credentialId:'dockerhub_info']
					usernameVariable:'DH_USER'
					passwordVariable:'DH_PASS'
				)]){
					sh '''
						eho "$DH_PASS" | docker login -u "$DH_USER" --password-stdin "$DH_PASS"
					   '''
				}
			}
		}
		
		stage('Docker Push') {
			steps {
				sh '''
					docker push jeunju528/ai-app:lastet
				   '''
			}
		}
		
		stage('Container Stop'){
			steps {
				sh '''
					docker stop ai-app || true
				   '''
			}
		}
		
		stage('Container Remove'){
			steps {
				sh '''
					docker rm ai-app || true
				   '''
			}
		}
		
		stage('Dockerhub pull'){
			steps {
				sh '''
					docker pull jeunju528/ai-app:lastet
				   '''
			}
		}
		
		stage('Docker run'){
			steps {
				sh '''
					docker run -d --name ai-app -p 9090:9090 --env-file .env jeunju528/ai-app:lastet
				   '''
			}
		}
	}
}