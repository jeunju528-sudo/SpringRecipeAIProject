pipeline {
	agent any
	
	environment {
		APP_DIR = "~/app"
		JAR_NAME = "SpringRecipeAIProject-0.0.1-SNAPSHOT.jar"
		DOCKER_IMAGE = "jeunju528/ai-app:lastet"
	}
	
	stages {
		stage("Repository Checkout"){
			steps {
				echo 'Git Checkout'
				checkout scm
			}
		}
		
		stage("Create .env"){
			steps {
				withCredentials([
					string(
						credentailsId: 'post-url'
						variable: 'POST_URL'
					),
					string(
						credentailsId: 'gen-key'
						variable: 'GEN_KEY'
					)
				]){
					sh '''
						echo "SPRING_PROFILES_ACTIVE=prod" > .env
						echo "POST_URL=${POST_URL} >> .env
						echo "GEN_KEY=${GEN_KEY} >> .env
						chmod 600 .env
					   '''
				}
			}
		}
		
		stage("Gradle Permission") {
			steps {
				sh '''
					./gradlew clean build -x test
				   '''
			}
		}
		
		stage("Docker build") {
			steps {
				sh '''
					docker build -t ${DOCKER_IMAGE} .
				   '''
			}
		}
		
		stage("DockerHub login") {
			steps {
				withCredentials([
					usernamePassword(
						credentailsId: 'dockerhub_info',
						usernameVariable: 'DH_USER',
						passwordVariable: 'DH_PASS'
					)
				]){
					sh '''
						echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
					   '''
				}
			}
		}
		
		stage("DockerHub Push"){
			steps {
				sh '''
					docker push ${DOCKER_IMAGE}
				   '''
			}
		}
		
		stage("Docker Compose DOWN"){
			steps {
				sh '''
					docker compose down || true
				   '''
			}
		}
		
		stage("DockerHub Pull"){
			steps {
				sh '''
					docker compose pull
				   '''
			}
		}
		
		stage("Docker Compose Run"){
			steps {
				sh '''
					docker compose up -d
				   '''
			}
		}

		stage("Container Check"){
			steps {
				sh '''
					docker compose ps
				   '''
			}
		}
	}
}
post {
	success {
		echo '==================='
		echo 'Docker Compose 배포 성공'
		echo '==================='
	}
	failure {
		echo '==================='
		echo 'Docker Compose 배포 실패'
		echo '==================='
		sh '''
			docker compose ps || true
		   '''
	}
}