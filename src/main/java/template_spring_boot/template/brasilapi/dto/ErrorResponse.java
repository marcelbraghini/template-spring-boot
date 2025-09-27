package template_spring_boot.template.brasilapi.dto;

public class ErrorResponse {
    private String message;
    private String name;
    private String type;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, String name, String type) {
        this.message = message;
        this.name = name;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

