[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18028175&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  

#### **Objective**  
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---
### **📥 Example Input**  

You will be given multiple text documents. Each document will contain several words. Your task is to compute the **Jaccard Similarity** between all pairs of documents based on the set of words they contain.  

# 📏 Jaccard Similarity Calculator

## Overview

The Jaccard Similarity is a statistic used to gauge the similarity and diversity of sample sets. It is defined as the size of the intersection divided by the size of the union of two sets.

## Formula

The Jaccard Similarity between two sets A and B is calculated as:

```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```

Where:
- `|A ∩ B|` is the number of words common to both documents
- `|A ∪ B|` is the total number of unique words in both documents

## Example Calculation

Consider two documents:
 
**doc1.txt words**: `{hadoop, is, a, distributed, system}`
**doc2.txt words**: `{hadoop, is, used, for, big, data, processing}`

- Common words: `{hadoop, is}`
- Total unique words: `{hadoop, is, a, distributed, system, used, for, big, data, processing}`

Jaccard Similarity calculation:
```
|A ∩ B| = 2 (common words)
|A ∪ B| = 10 (total unique words)

Jaccard Similarity = 2/10 = 0.2 or 20%
```

## Use Cases

Jaccard Similarity is commonly used in:
- Document similarity detection
- Plagiarism checking
- Recommendation systems
- Clustering algorithms

## Implementation Notes

When computing similarity for multiple documents:
- Compare each document pair
- Output pairs with similarity > 50%

### **📤 Expected Output**  

The output should show the Jaccard Similarity between document pairs in the following format:  
```
(doc1, doc2) -> 60%  
(doc2, doc3) -> 50%  
```

---

### Approach and Implementation
This section outlines the core components and methodology used in implementing the document similarity analysis system.
### Architecture Overview
The system is built on three fundamental components that work together to analyze document similarity. The DocumentSimilarityDriver serves as the primary controller, configuring and managing the MapReduce job execution. The DocumentSimilarityMapper component handles the processing of individual documents and term extraction. The DocumentSimilarityReducer takes care of combining the processed data and performing similarity calculations between documents.
### How it Works
## Mapper Implementation
The Mapper component implements a systematic approach to document processing. It begins by reading documents line by line, accumulating the complete text content of each document. Once the content is gathered, it undergoes a series of processing steps. The text is split into individual words, which are then normalized by converting to lowercase and removing any special characters. These processed words are collected into a set of unique terms. Finally, the Mapper outputs a record containing the document's identifier along with its list of unique terms.
## Reducer Implementation
The Reducer component processes the aggregated document records to determine similarity between documents. For each possible pair of documents in the input set, it first parses their respective term lists. It then applies the Jaccard similarity coefficient to calculate the similarity between the document pairs. The final output consists of similarity scores for all document pairs, providing a comprehensive comparison across the document set.
### Running the Project
Prerequisites:
```
The project requires specific software components for successful execution. Java 8 or a higher version is needed as the primary programming platform. Hadoop 3.x must be installed and properly configured as the distributed processing framework. Maven is required for building the project and managing dependencies.
```
### **🛠 Instructions**  

Since we are using **Docker Compose** to run a Hadoop cluster, follow these steps to set up your environment.  

#### ** Install Docker & Docker Compose**  
- **Windows**: Install **Docker Desktop** and enable WSL 2 backend.  
- **macOS/Linux**: Install Docker using the official guide: [Docker Installation](https://docs.docker.com/get-docker/)  

### Project Execution Commands

### Start the Docker containers:
```
docker compose up -d
```
### Build the project using Maven:
```
mvn install
```
### Move the compiled JAR to the shared folder:
```
mv target/*.jar shared-folder/input/code/
```
### Copy necessary files to the resource manager container:

### Copy the application JAR
```
docker cp shared-folder/input/code/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```
# Copy the sample documents
```
docker cp shared-folder/input/example/doc1.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp shared-folder/input/example/doc2.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp shared-folder/input/example/doc3.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```
### Running the MapReduce Job

### Connect to the resource manager container:
```
docker exec -it resourcemanager /bin/bash
```
### Navigate to the MapReduce directory:
```
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```
### Create HDFS input directory and upload data:

# Create input directory
```
hadoop fs -mkdir -p /input/dataset
```
# Upload sample documents
```
hadoop fs -put ./doc1.txt /input/dataset
hadoop fs -put ./doc2.txt /input/dataset
hadoop fs -put ./doc3.txt /input/dataset
```
### Execute the MapReduce job:
```
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input/dataset /output
```
### Viewing and Retrieving Results
```
hadoop fs -cat /output/*
```
### Copy results to local filesystem within container:
```
hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```
### Exit the container:
```
exit
```
### Copy results from container to host machine:
```
docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ shared-folder/output/
```
#### **Example Documents**  

##### **doc1.txt**  
```
cat, dog, rabbit
```

##### **doc2.txt**  
```
cat, dog, lion
```

##### **doc3.txt**  
```
cat, dog, rabbit, lion
```
## ** Output **
```
doc2.txt, doc3.txt	Similarity: 0.75
doc3.txt, doc1.txt	Similarity: 0.75
```
---

### challenges faced 
1. Computing similarities between document pairs 
 Implemented a unified reducer with optimized comparison logic that uses early termination for dissimilar documents.
 2. Need to handle streaming updates to similarity calculations as new documents arrive.
 Implement incremental similarity updates that can efficiently update existing results when new documents are added.