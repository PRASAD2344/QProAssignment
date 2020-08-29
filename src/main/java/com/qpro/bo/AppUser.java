package com.qpro.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements Serializable {
    private String remoteAddress;
    private Set<Long> topItemsServed;
}
