package center.jhub.dev.controller;

import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_CAN_BE_NEGATIVE_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_MAX_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_MIN_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_TYPE_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_VALUE_PROPERTY;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import center.jhub.dev.service.DevService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
public class DevController {

    private final DevService devService;

    public DevController(DevService devService) {
        this.devService = devService;
    }


    @PostMapping({ "", "/" })
    @Operation(
        summary = "Returns an object generated from the template given",
        description = "Generates an object based on the template given. It accepts literal objects or"
                          + " or templates. literal objects return default values for strings, numbers,"
                          + " lists, etc.\nIf specific formats are needed, use template.\n\nTemplate"
                          + " definition:\n\n"
                          + "\n\n"
                          + "- **" + JSON_TYPE_PROPERTY + "**: `STRING|BOOLEAN|INTEGER|LONG|DECIMAL|CHARACTER|SHORT|LIST|ARRAY|OBJECT`\n"
                          + "- **" + JSON_MAX_PROPERTY + "**: `" + Integer.MIN_VALUE + "~" + Integer.MAX_VALUE + "`\n"
                          + "  - Has to be equal or higher than **" + JSON_MIN_PROPERTY
                          + "** and in some types, such as `STRING`, `CHARACTER`, `LIST` or `ARRAY` it can't be less than 0.\n"
                          + "  - This is ignored for types `BOOLEAN` and `OBJECT`\n"
                          + "- **" + JSON_MIN_PROPERTY + "**: `" + Integer.MIN_VALUE + "~" + Integer.MAX_VALUE + "`\n"
                          + "  - Has to be equal or lower than **" + JSON_MAX_PROPERTY + "** and in some types, such as "
                          + "`STRING`, `CHARACTER`, `LIST` or `ARRAY` it can't be less than 0."
                          + "  - This is ignored for types `BOOLEAN` and `OBJECT`\n"
                          + "- **" + JSON_VALUE_PROPERTY + "**:\n  - The actual object or array/list or template that represents it\n"
                          + "- **" + JSON_CAN_BE_NEGATIVE_PROPERTY + "**: `true|false`\n  - When **" + JSON_MIN_PROPERTY
                          + "** is not given but there's a need to specify if the generated number can be negative.",
                  requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                      content = @Content(
                          mediaType = MediaType.APPLICATION_JSON_VALUE,
                          examples = {
                              @ExampleObject(name = "Template",
                                  summary = "Template",
                                  value = "{"
                                                 + "\"id\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"LONG\","
                                                 + "\"" + JSON_MAX_PROPERTY + "\": 1000000,"
                                                 + "\"" + JSON_CAN_BE_NEGATIVE_PROPERTY + "\": false"
                                                 + "},"
                                                 + "\"customer\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"OBJECT\","
                                                 + "\"" + JSON_VALUE_PROPERTY + "\": {"
                                                 + "\"age\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"INTEGER\","
                                                 + "\"" + JSON_MAX_PROPERTY + "\": 100,"
                                                 + "\"" + JSON_CAN_BE_NEGATIVE_PROPERTY + "\": false"
                                                 + "},"
                                                 + "\"name\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"STRING\","
                                                 + "\"" + JSON_MAX_PROPERTY + "\": 250"
                                                 + "}"
                                                 + "},"
                                                 + "\"products\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"ARRAY\","
                                                 + "\"" + JSON_MAX_PROPERTY + "\": 10,"
                                                 + "\"" + JSON_VALUE_PROPERTY + "\": {"
                                                 + "\"" + JSON_TYPE_PROPERTY + "\": \"STRING\","
                                                 + "\"" + JSON_MIN_PROPERTY + "\": 10,"
                                                 + "\"" + JSON_MAX_PROPERTY + "\": 100"
                                                 + "}"
                                                 + "}"
                                                 + "}"
                                                 + "}"),
                              @ExampleObject(name = "Plain Object",
                                  summary = "Plain object",
                                  value = "{"
                                                 + "\"id\": 1000,"
                                                 + "\"customer\": {"
                                                 + "\"age\": 30,"
                                                 + "\"name\": \"John Doe\""
                                                 + "},"
                                                 + "\"products\": [\"carrots\", \"apples\"]"
                                                 + "}")
                          }
                      )
                  ),
                  responses = {
            @ApiResponse(
                description = "Sends back a generated response from the template or plain object sent.",
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                examples = {
                                    @ExampleObject(name = "Plain Object",
                                        summary = "Plain object",
                                        value = "{"
                                                    + "\"id\": 1000,"
                                                    + "\"customer\": {"
                                                    + "\"age\": 30,"
                                                    + "\"name\": \"John Doe\""
                                                    + "},"
                                                    + "\"products\": [\"carrots\", \"apples\"]"
                                                    + "}")
                                }
                            )
            )
                  }
    )
    public ResponseEntity<?> postRest(@RequestBody DevRestInDTO dto) {
        return ResponseEntity.ok(devService.getRest(dto));
    }
}
