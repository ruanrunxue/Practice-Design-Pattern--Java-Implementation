package com.yrunz.designpattern.service.registry.model.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.service.registry.model.ServiceProfile;

import java.util.ArrayList;
import java.util.List;

// profile表遍历, 筛选符合ServiceId和ServiceType的记录
public class ServiceProfileVisitor implements TableVisitor<ServiceProfile> {
    private String serviceId;
    private String serviceType;

    private ServiceProfileVisitor() {
        this.serviceId = "";
        this.serviceType = "";
    }

    public static ServiceProfileVisitor create() {
        return new ServiceProfileVisitor();
    }

    @Override
    public List<ServiceProfile> visit(Table<?, ServiceProfile> table) {
        List<ServiceProfile> result = new ArrayList<>();
        TableIterator<ServiceProfile> iterator = table.iterator();
        while (iterator.hasNext()) {
            ServiceProfile serviceProfile = iterator.next();
            // 先匹配ServiceId，如果一致则无须匹配ServiceType
            String serviceId = serviceProfile.id();
            if (!serviceId.equals("") && serviceId.equals(this.serviceId)) {
                result.add(serviceProfile);
                continue;
            }
            // ServiceId匹配不上，再匹配ServiceType
            String serviceType = serviceProfile.type();
            if (!serviceType.equals("") && serviceType.equals(this.serviceType)) {
                result.add(serviceProfile);
            }
        }
        return result;
    }

    public void addServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void addServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
