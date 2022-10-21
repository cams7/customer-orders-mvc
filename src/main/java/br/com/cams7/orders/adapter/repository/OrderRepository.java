package br.com.cams7.orders.adapter.repository;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;

import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import br.com.cams7.orders.core.utils.DateUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository
    implements GetOrdersByCountryRepositoryPort,
        GetOrderByIdRepositoryPort,
        DeleteOrderByIdRepositoryPort,
        CreateOrderRepositoryPort,
        UpdateShippingByIdRepositoryPort {

  private final DateUtils dateUtils;
  private final MongoTemplate mongoTemplate;
  private final ModelMapper modelMapper;

  @Override
  public List<OrderEntity> getOrders(final String country) {
    final var query = new Query(Criteria.where("address.country").is(country));
    return mongoTemplate.find(query, OrderModel.class, getCollectionName(country)).stream()
        .map(this::getOrderWithoutOptional)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<OrderEntity> getOrder(final String country, final String orderId) {
    return getOrder(mongoTemplate.findById(orderId, OrderModel.class, getCollectionName(country)));
  }

  @Override
  public Optional<Long> delete(final String country, final String orderId) {
    final var query = new Query(Criteria.where("id").is(orderId));
    return Optional.of(
        mongoTemplate
            .remove(query, OrderModel.class, getCollectionName(country))
            .getDeletedCount());
  }

  @Override
  public Optional<OrderEntity> create(final String country, final OrderEntity order) {
    return getOrder(mongoTemplate.insert(getOrder(order), getCollectionName(country)));
  }

  @Override
  public Optional<Long> updateShipping(
      final String country, final String orderId, final Boolean registeredShipping) {
    final var query = new Query(Criteria.where("id").is(orderId));
    final var update = new Update();
    update.set("registeredShipping", registeredShipping);
    return Optional.of(
        mongoTemplate
            .updateFirst(query, update, OrderModel.class, getCollectionName(country))
            .getModifiedCount());
  }

  private Optional<OrderEntity> getOrder(final OrderModel model) {
    if (model == null) return Optional.empty();
    final var order = getOrderWithoutOptional(model);
    return Optional.of(order);
  }

  private OrderEntity getOrderWithoutOptional(final OrderModel model) {
    final var country = model.getAddress().getCountry();
    final var entity =
        modelMapper
            .map(model, OrderEntity.class)
            .withOrderId(model.getId())
            .withTotalAmount(model.getTotal())
            .withRegistrationDate(dateUtils.getZonedDateTime(country, model.getRegistrationDate()));
    return entity;
  }

  private OrderModel getOrder(OrderEntity entity) {
    final var model = modelMapper.map(entity, OrderModel.class);
    model.setId(entity.getOrderId());
    model.setTotal(entity.getTotalAmount());
    model.setRegistrationDate(entity.getRegistrationDate().toLocalDateTime());
    return model;
  }

  private static String getCollectionName(String country) {
    return getCollectionByCountry(country, COLLECTION_NAME);
  }
}
