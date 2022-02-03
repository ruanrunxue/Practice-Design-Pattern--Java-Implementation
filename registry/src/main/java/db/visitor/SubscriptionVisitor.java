package db.visitor;

import db.Table;
import db.TableIterator;
import db.TableVisitor;
import domain.Subscription;

import java.util.ArrayList;
import java.util.List;

// 订阅表遍历, 筛选符合targetServiceId和targetServiceType的订阅记录
public class SubscriptionVisitor implements TableVisitor<Subscription> {
    private String targetServiceId;
    private String targetServiceType;

    private SubscriptionVisitor() {
        targetServiceId = "";
        targetServiceType = "";
    }

    public static SubscriptionVisitor create() {
        return new SubscriptionVisitor();
    }

    public SubscriptionVisitor withTargetServiceId(String targetServiceId) {
        this.targetServiceId = targetServiceId;
        return this;
    }

    public SubscriptionVisitor withTargetServiceType(String targetServiceType) {
        this.targetServiceType = targetServiceType;
        return this;
    }

    @Override
    public List<Subscription> visit(Table<?, Subscription> table) {
        List<Subscription> result = new ArrayList<>();
        TableIterator<Subscription> iterator = table.iterator();
        while (iterator.hasNext()) {
            Subscription subscription = iterator.next();
            // 先匹配ServiceId，如果一致则无须匹配ServiceType
            String targetServiceId = subscription.targetServiceId();
            if (!targetServiceId.equals("") && targetServiceId.equals(this.targetServiceId)) {
                result.add(subscription);
                continue;
            }
            // ServiceId匹配不上，再匹配ServiceType
            String targetServiceType = subscription.targetServiceType();
            if (!targetServiceType.equals("") && targetServiceType.equals(this.targetServiceType)) {
                result.add(subscription);
            }
        }
        return result;
    }
}
