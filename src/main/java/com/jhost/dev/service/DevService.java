package com.jhost.dev.service;

import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_LIST_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_MAX_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_MIN_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_TYPE_PROPERTY;
import static center.jhub.data.dto.in.dev.RestOptionsInDTO.JSON_VALUE_PROPERTY;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import center.jhub.data.dto.in.dev.RestOptionsInDTO;
import center.jhub.data.dto.in.dev.RestOptionsInDTO.FieldType;
import center.jhub.utils.FileUtils;
import center.jhub.utils.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhost.dev.config.Constants;
import com.jhost.dev.service.meta.MessageService;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DevService {

    private final MessageService messageService;
    private final String DEFAULT_TEXT_FILE = "text.txt";
    private final int DEFAULT_MAX_LENGTH = 255;
    private final int DEFAULT_MIN_NO_NEG = 0;
    private final int DEFAULT_MIN = Integer.MIN_VALUE;
    private final int DEFAULT_MAX = Integer.MAX_VALUE;
    private String textCache;
    private String digits = "0123456789";
    private int maxLength = -1;
    private Random random;
    private ObjectMapper mapper = ObjectMappers.getInstance();

    public DevService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getTestMessage(Locale locale){
        return messageService.getMessage(Constants.MessagePaths.TEST_MESSAGE, locale);
    }

    public Object getRest(DevRestInDTO dto) {
        DevRestInDTO out = new DevRestInDTO();
        dto.forEach((k, v) -> out.put(k, getExampleForType(v, k)));
        return out;
    }

    private Object getExampleForType(Object type, String fieldName) {
        if (type instanceof Map m) {
            try {
                RestOptionsInDTO r = mapper.convertValue(m, RestOptionsInDTO.class);
                return isGenericObject(r) ?
                           getExampleForObject(m)
                           : getExampleForOptions(r, fieldName);
            } catch (IllegalArgumentException ignore) {
                return getExampleForObject(m);
            }
        }
        if (type instanceof List<?> l) return getExampleList(l, fieldName);
        if (type instanceof String) return getExampleString();
        if (type instanceof Integer) return getExampleInt();
        if (type instanceof Boolean) return getExampleBoolean();
        return getExampleString();
    }

    private boolean isGenericObject(RestOptionsInDTO r) {
        return Objects.isNull(r.getType()) &&
                   Objects.isNull(r.getMin()) &&
                   Objects.isNull(r.getMax());
    }

    private Object getExampleForObject(Object o, String fieldName) {
        if (o instanceof Map m) return getExampleForObject(m);
        return getExampleForType(o, fieldName);
    }

    private Map<String, Object> getExampleForObject(Map<String, Object> m) {
        return m.entrySet().stream()
                   .map(e -> {
                       e.setValue(this.getExampleForType(e.getValue(), e.getKey()));
                       return e;
                   })
                   .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Object getExampleForOptions(RestOptionsInDTO r, String fieldName) {
        verifyRestOptions(r, fieldName);
        return switch (r.getType()) {
            case STRING -> getExampleString(r);
            case INTEGER -> getExampleInt(r);
            case BOOLEAN -> getExampleBoolean();
            case LONG -> getExampleInt(r);
            case DECIMAL -> getExampleInt(r);
            case CHARACTER -> getExampleString(r);
            case SHORT -> getExampleString(r);
            case ARRAY, LIST -> getExampleList(r, fieldName);
            case OBJECT -> getExampleForObject(r.getValue(), fieldName);
            default -> getExampleString();
        };
    }

    private Object getExampleList(RestOptionsInDTO options, String fieldName) {
        if (!optionsHasMinMax(options)) {
            if (options.getValue() instanceof List<?> l) {
                return getExampleList(l, fieldName);
            }
            throw new RuntimeException("Invalid value for \"" + JSON_VALUE_PROPERTY + "\" must be a list of items or \"" +
                                          JSON_MIN_PROPERTY + "\" and \"" + JSON_MAX_PROPERTY + "\" properties must be defined for field \"" + fieldName + "\"");
        }
        int size = getExampleInt(Math.max(DEFAULT_MIN_NO_NEG, options.getMin()), options.getMax());

        List<Object> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(getExampleForType(options.getValue(), fieldName));
        }

        return list;
    }

    private List<Object> getExampleList(List<?> l, String fieldName) {
        return l.stream().map(e -> this.getExampleForType(e, fieldName)).toList();
    }

    private void verifyRestOptions(RestOptionsInDTO r, String fieldName) {
        if (Objects.isNull(r.getType())) {
            throw new RuntimeException("Field is not typified. Field \"" + JSON_TYPE_PROPERTY + "\" is not defined for field \"" + fieldName + "\"");
        }
        if ((FieldType.ARRAY.equals(r.getType()) || FieldType.LIST.equals(r.getType())) && (Objects.isNull(r.getValue()))) {
            throw new RuntimeException("Field is type LIST or ARRAY, but no \"" + JSON_VALUE_PROPERTY + "\" value was given for field \"" + fieldName + "\"");
        }
        if (FieldType.OBJECT.equals(r.getType()) && (Objects.isNull(r.getValue()))) {
            throw new RuntimeException("Field is type OBJECT, but no \"" + JSON_VALUE_PROPERTY + "\" value was given for field \"" + fieldName + "\"");
        }
        if (Objects.nonNull(r.getMin()) && Objects.nonNull(r.getMax()) && r.getMin() >= r.getMax()) {
            throw new RuntimeException("Max value \"" + r.getMax() + "\" is lower then min value \"" + r.getMin() + "\" for field \"" + fieldName + "\"");
        }
    }

    private boolean getExampleBoolean() {
        return getRandom().nextBoolean();
    }

    private String getExampleString(RestOptionsInDTO options) {
        if (optionsHasMinMax(options)) return getExampleString(options.getMin(), options.getMax());
        return getExampleString();
    }

    private String getExampleString() {
        return getExampleString(1, DEFAULT_MAX_LENGTH);
    }

    private String getExampleString(int min, int max) {
        int size = getRandom().nextInt(min, Math.min(max, getMaxLength()));
        int start = getRandom().nextInt(0, getMaxLength() - size);

        return getText().substring(start, start + size);
    }

    private int getExampleInt(RestOptionsInDTO options) {
        if (!optionsHasMinMax(options)) {
            if (Objects.nonNull(options.getCanBeNegative())) {
                return getExampleInt(options.getCanBeNegative());
            }
            return getExampleInt();
        }
        return getExampleInt(options.getMin(), options.getMax());
    }

    private int getExampleInt() {
        return getExampleInt(Boolean.FALSE);
    }

    private int getExampleInt(boolean canBeNegative) {
        if (canBeNegative) return getExampleInt(DEFAULT_MIN_NO_NEG, DEFAULT_MAX);
        return getExampleInt(DEFAULT_MIN, DEFAULT_MAX);
    }

    private int getExampleInt(int min, int max) {
        return getRandom().nextInt(min, max);
    }

    private Random getRandom() {
        if (Objects.isNull(this.random)) {
            this.random = new Random(42069L);
        }
        return this.random;
    }

    private String getText() {
        String fileName = DEFAULT_TEXT_FILE;
        if (Objects.isNull(this.textCache)) {
            FileUtils.doOnFile(fileName, s -> this.textCache = s);
        }
        return this.textCache;
    }

    private int getMaxLength() {
        if (maxLength < 0) {
            maxLength = getText().length();
        }
        return maxLength;
    }

    private boolean optionsHasMinMax(RestOptionsInDTO options) {
        return Objects.nonNull(options.getMin()) && Objects.nonNull(options.getMax());
    }
}
