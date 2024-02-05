module com.example.chatapplicationclientjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatapplicationclientjavafx to javafx.fxml;
    exports com.example.chatapplicationclientjavafx;
}