package net.dzakirin.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseListResponse<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private long totalRecords;
    private int totalPages;
}
