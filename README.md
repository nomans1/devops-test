# Minikube Setup with Jenkins Namespace

This guide will help you set up a local Kubernetes environment using **Minikube** and **kubectl** on **macOS**. We'll also create a Kubernetes namespace named `jenkins` to isolate Jenkins-related resources.

---

## 🧰 Prerequisites

- macOS with **Homebrew** installed
- Virtualization enabled (Minikube can use Docker, HyperKit, etc.)

---

## Step 1: Install `kubectl` & `minikube`

Install the Kubernetes command-line tool:

```bash
brew install kubectl

Install the minikube:

```bash
brew install minikube
