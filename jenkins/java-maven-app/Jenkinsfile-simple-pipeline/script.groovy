def buildJar() {
    echo "building the application..."
    sh 'mvn -f "jenkins/java-maven-app/" package'
} 

def buildImage() {
    echo "building the docker image..."
    app = docker.build("sws-globalsre-cug01-qa/java-maven-app:2.0", "-f jenkins/java-maven-app/Dockerfile .")
    docker.withRegistry('https://us.gcr.io', 'gcr:sws-globalsre-cug01-qa') {
        app.push()
    }
} 

def deployApp() {
    echo 'deploying the application...'
} 

return this
