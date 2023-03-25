Curl commands for testing MealRestController methods. For new row use "/\":

1. Meal create(Meal)

   curl -X POST http://localhost:8080/topjava/rest/meals \
   -v -H "Content-Type:application/json;charset=UTF-8" \
   -d '{"id":null,  "dateTime":"2023-01-31T10:00:00", "description":"Перекус", "calories":500}' | json_pp

2. Meal get(int id)

   curl http://localhost:8080/topjava/rest/meals/100003 -v -H "Content-Type:application/json;charset=UTF-8" | json_pp

3. List<MealTo> getAll()

   curl http://localhost:8080/topjava/rest/meals \
   -v -H "Content-Type:application/json;charset=UTF-8" | json_pp

4. List<MealTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime)

   curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-30&endTime=20:00:00" \
   -v -H "Content-Type:application/json;charset=UTF-8" | json_pp

5. Meal update(Meal)

   curl -X PUT http://localhost:8080/topjava/rest/meals/100007 \
   -v -H "Content-Type:application/json;charset=UTF-8" \
   -d '{"id":100007,  "dateTime":"2023-12-31T10:00:00", "description":"Перекус", "calories":500}' | json_pp

6. void delete(int id)

   curl -X DELETE http://localhost:8080/topjava/rest/meals/100007 -v | json_pp
