package br.com.cams7.orders.adapter.repository;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;

import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.utils.DateUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository
    implements GetOrdersByCountryRepositoryPort,
        GetOrderByIdRepositoryPort,
        DeleteOrderByIdRepositoryPort,
        CreateOrderRepositoryPort {

  private final DateUtils dateUtils;
  private final MongoTemplate mongoTemplate;
  private final ModelMapper modelMapper;

  @Override
  public List<OrderEntity> getOrders(String country) {
    var query = new Query(Criteria.where("address.country").is(country));
    return mongoTemplate.find(query, OrderModel.class, getCollectionName(country)).stream()
        .map(this::getOrder)
        .collect(Collectors.toList());
  }

  @Override
  public OrderEntity getOrder(String country, String orderId) {
    return getOrder(mongoTemplate.findById(orderId, OrderModel.class, getCollectionName(country)));
  }

  @Override
  public Long delete(String country, String orderId) {
    var query = new Query(Criteria.where("id").is(orderId));
    return mongoTemplate
        .remove(query, OrderModel.class, getCollectionName(country))
        .getDeletedCount();
  }

  @Override
  public OrderEntity create(String country, OrderEntity order) {
    return getOrder(mongoTemplate.insert(getOrder(order), getCollectionName(country)));
  }

  private OrderEntity getOrder(OrderModel model) {
    if (model == null) return null;
    var country = model.getAddress().getCountry();
    var entity =
        modelMapper
            .map(model, OrderEntity.class)
            .withOrderId(model.getId())
            .withTotalAmount(model.getTotal())
            .withRegistrationDate(dateUtils.getZonedDateTime(country, model.getRegistrationDate()));
    return entity;
  }

  private OrderModel getOrder(OrderEntity entity) {
    if (entity == null) return null;
    var model = modelMapper.map(entity, OrderModel.class);
    model.setId(entity.getOrderId());
    model.setTotal(entity.getTotalAmount());
    model.setRegistrationDate(entity.getRegistrationDate().toLocalDateTime());
    return model;
  }

  private static String getCollectionName(String country) {
    return getCollectionByCountry(country, COLLECTION_NAME);
  }
}
