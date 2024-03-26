# MAS-Cloud+

A Multi-agent Architecture for Optimized Cloud Resource Management

The project aims at managing virtual machines in cloud providers.

## Tools used for development:

- **Java Development Kit (JDK)** 1.8 or newer
- Java IDE (Eclipse IDE, NetBeans, or IntelliJ IDEA)
- **Vagrant** by HashiCorp - open-source software for creating and maintaining portable virtual development environments
- **Drools JBoss** - Drools is a business rules management system with a rules engine based on chaining inference
- **Drools Jade** - JAVA Agent DEvelopment Framework
- **openCSV** - A Java library for reading and writing CSV files
- **Python** - required for running OR-Tools
- **OR-Tools** - an open-source software suite for optimization

## Usage Steps:

1. **Setting up Development Environment:**
   - Use a Java IDE of your choice.
   - Install dependencies:
     - Drools JBoss 6.5.0-final
     - Drools Jade - JAVA Agent DEvelopment Framework
     - openCSV
   - Make sure JDK 1.8 or newer is installed on your system.

2. **Setting up Execution Environment (Local and Cloud):**
   - Install Vagrant on your local machine.
   - After installing Vagrant, install the plugins that connect to cloud providers:
     - AWS: [vagrant-aws](https://github.com/mitchellh/vagrant-aws)
     - Google Cloud: [vagrant-google](https://github.com/mitchellh/vagrant-google)
     - Azure: [vagrant-azure](https://github.com/Azure/vagrant-azure)
   - Configure VM options in the `src/Starter/Starter.java` file as needed, along with access keys to the chosen provider.

3. **Setting up Execution Environment (Local):**
   - Install Python on your local machine.
   - Install OR-Tools, a Python library for optimization.

4. **Configuring Virtual Machines in the Cloud:**
   - On each virtual machine created in the cloud, it is necessary to install `dstat` for monitoring.

