# MAS-Cloud+

## Overview

MAS-Cloud+ is a comprehensive solution for managing virtual machines (VMs) efficiently in various cloud providers. It utilizes multi-agent architecture for optimized resource allocation and management.

## Development Environment Setup

To contribute or modify MAS-Cloud+, follow these steps to set up your development environment:

- **Java Development Kit (JDK):** Ensure you have JDK 1.8 or newer installed on your system.
- **Java IDE:** Choose your preferred Java Integrated Development Environment (IDE) such as Eclipse IDE, NetBeans, or IntelliJ IDEA.
- **Dependencies:**
  - Install Drools JBoss 6.5.0-final, a powerful business rules management system.
  - Install Drools Jade, a JAVA Agent DEvelopment Framework.
  - Install openCSV, a Java library for reading and writing CSV files.

## Execution Environment Setup (Local and Cloud)

MAS-Cloud+ supports both local and cloud execution environments. Follow these steps to set up your execution environment:

- **Vagrant Setup:**
  - Install Vagrant, an open-source software for managing virtual environments.
  - Install the necessary Vagrant plugins for connecting to your preferred cloud provider:
    - AWS: [vagrant-aws](https://github.com/mitchellh/vagrant-aws)
    - Google Cloud: [vagrant-google](https://github.com/mitchellh/vagrant-google)
    - Azure: [vagrant-azure](https://github.com/Azure/vagrant-azure)
- **Configuration:**
  - Customize VM configurations in the `src/Starter/Starter.java` file according to your requirements.
  - Provide access keys and credentials for the chosen cloud provider.

## Local Execution Environment Setup

For local execution, follow these additional steps:

- **Python Installation:**
  - Install Python on your local machine.
- **OR-Tools Setup:**
  - Install OR-Tools, an open-source optimization suite, using Python.

## Cloud VM Configuration

When provisioning virtual machines in the cloud, ensure the following steps are performed:

- **dstat Installation:**
  - Install dstat on each virtual machine to monitor system resource usage effectively.

## Additional Dependencies

- **dstat:** A powerful, flexible, and versatile utility for generating Linux system resource statistics, serving as a replacement for all the tools mentioned above.

## Contribution

Contributions to MAS-Cloud+ are welcome! Feel free to fork the repository, make your changes, and submit a pull request.

