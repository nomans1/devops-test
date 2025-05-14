#!/bin/bash

set -e

echo "🔧 Checking prerequisites..."

# Check for Homebrew
if ! command -v brew &>/dev/null; then
  echo "❌ Homebrew is not installed. Please install Homebrew first: https://brew.sh"
  exit 1
fi

# Install kubectl if not installed
if ! command -v kubectl &>/dev/null; then
  echo "📦 Installing kubectl..."
  brew install kubectl
else
  echo "✅ kubectl is already installed."
fi

# Install minikube if not installed
if ! command -v minikube &>/dev/null; then
  echo "📦 Installing minikube..."
  brew install minikube
else
  echo "✅ minikube is already installed."
fi

# Install helm if not installed
if ! command -v helm &>/dev/null; then
  echo "📦 Installing Helm..."
  brew install helm
else
  echo "✅ Helm is already installed."
fi

echo "🚀 Starting Minikube..."

# Start Minikube with Docker driver
minikube start --driver=docker

echo "⏳ Waiting for Kubernetes node to be ready..."
kubectl wait --for=condition=Ready node/minikube --timeout=120s

echo "📂 Creating 'jenkins' namespace (if not exists)..."

# Create jenkins namespace if it doesn't exist
if ! kubectl get namespace jenkins &>/dev/null; then
  kubectl create namespace jenkins
  echo "✅ Namespace 'jenkins' created."
else
  echo "⚠️ Namespace 'jenkins' already exists. Skipping."
fi

echo "📥 Adding Jenkins Helm repository..."
helm repo add jenkins https://charts.jenkins.io
helm repo update

echo "📦 Installing Jenkins in the 'jenkins' namespace..."

# Install Jenkins via Helm
helm upgrade --install jenkins jenkins/jenkins -f values.yaml --namespace jenkins

echo "⏳ Waiting for Jenkins pods to be ready..."
kubectl rollout status statefulset/jenkins --namespace jenkins --timeout=180s || true

echo "🔑 Fetching Jenkins admin password..."
kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode && echo

echo "🌐 Port forwarding Jenkins service to http://localhost:8080"
echo "Press Ctrl+C to stop port forwarding."

kubectl --namespace jenkins port-forward svc/jenkins 8080:8080
