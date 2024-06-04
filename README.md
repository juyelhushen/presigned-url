# Pre-Signed URL File Service

This project provides a simple file service that generates pre-signed URLs for uploading and downloading files to and from an AWS S3 bucket. The service includes endpoints for generating PUT and GET pre-signed URLs.

## Project Structure

- **FileService**: Service class responsible for generating pre-signed URLs.
- **FileController**: REST controller that exposes endpoints to generate pre-signed URLs.
- **S3Config**: Configuration class for AWS S3 client setup.
- **application.properties**: Configuration file for server settings and AWS credentials.
- **pom.xml**: Maven configuration file with necessary dependencies.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven
- AWS account with access to S3
- AWS S3 bucket created and accessible

### Configuration

Update the `application.properties` file with your AWS credentials and S3 bucket details:

```properties
server:
  port: 9001

# AWS credentials
aws:
  accessKey: <ACCESS_KEY>
  secretKey: <SECRET_KEY>
  region: ap-northeast-1
  s3:
    bucket: presigneds3test
```
Replace <ACCESS_KEY> and <SECRET_KEY> with your actual AWS access key and secret key and others also.

# Dependencies
Ensure the following dependencies are included in your pom.xml:
```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk-s3</artifactId>
        <version>1.11.163</version>
    </dependency>
</dependencies>
```
# Running the Application
To run the application, use the following command:
```
mvn spring-boot:run
```
The application will start on port 9001 as specified in the application.properties file. 
# Endpoints
Generate PUT Pre-Signed URL
Endpoint to generate a pre-signed URL for uploading a file to S3.

URL: /geturl
Method: POST
Request Param: extension (String) - The file extension for the file to be uploaded.

## Example Request:
```
curl -X POST "http://localhost:9001/geturl?extension=txt"
```
Generate GET Pre-Signed URL
Endpoint to generate a pre-signed URL for downloading a file from S3.

URL: /getpdfurl
Method: GET
Request Param: filename (String) - The name of the file to be downloaded.
## Example Request:
```
curl -X GET "http://localhost:9001/getpdfurl?filename=example.txt"
```
# Code Explanation
## FileService
This service class is responsible for generating pre-signed URLs.
```
@Service
public class FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String generatePreSignedUrl(String filePath, HttpMethod http) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 2);
        return amazonS3.generatePresignedUrl(bucketName, filePath, cal.getTime(), http).toString();
    }
}
```
## FileController
This controller exposes endpoints to generate pre-signed URLs for uploading and downloading files.
```
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/geturl")
    public ResponseEntity<String> generateUrl(@RequestParam String extension) {
        return ResponseEntity.ok(fileService.generatePreSignedUrl(UUID.randomUUID() + "." + extension, HttpMethod.PUT));
    }

    @GetMapping("/getpdfurl")
    public ResponseEntity<String> getUrl(@RequestParam String filename) {
        return ResponseEntity.ok(fileService.generatePreSignedUrl(filename, HttpMethod.GET));
    }
}
```
## S3Config
This configuration class sets up the AWS S3 client.
```
@Configuration
public class S3Config {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
```

Hope this helps! If you have any questions or need further assistance, feel free to reach out.

