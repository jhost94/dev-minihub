package center.jhub.dev.service;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import center.jhub.data.dto.in.dev.RestOptionsInDTO;
import center.jhub.data.dto.out.dev.DevRestOutDTO;
import center.jhub.dev.config.Constants;
import center.jhub.dev.service.meta.MessageService;
import center.jhub.utils.ThreadUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestService {

    private final MessageService messageService;
    private final TemplateService templateService;

    public String getTestMessage(Locale locale){
        return messageService.getMessage(Constants.MessagePaths.TEST_MESSAGE, locale);
    }

    public DevRestOutDTO objectRest(DevRestInDTO dto) {
        return objectRest(dto, 0);
    }


    public DevRestOutDTO objectRest(DevRestInDTO dto, int delay) {
        DevRestOutDTO out = new DevRestOutDTO();
        dto.forEach((k, v) -> out.put(k, templateService.getExampleForType(v, k)));
        ThreadUtils.sleep(Duration.ofSeconds(delay));
        return out;
    }

    public List<DevRestOutDTO> listRest(DevRestInDTO dto, Integer min, Integer max) {
        return listRest(dto, min, max, 0);
    }

    public List<DevRestOutDTO> listRest(DevRestInDTO dto, Integer min, Integer max, int delay) {
        int size = templateService.getExampleInt(min, max);
        List<DevRestOutDTO> out = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            out.add(objectRest(dto));
        }

        ThreadUtils.sleep(Duration.ofSeconds(delay));

        return out;
    }

    public Map<String, RestOptionsInDTO> generateTemplate(Map<String, Object> dto) {
        Map<String, RestOptionsInDTO> out = new HashMap<>(dto.size());
        dto.forEach((k, v) -> out.put(k, templateService.generateTemplateForType(v)));
        return out;
    }
}
