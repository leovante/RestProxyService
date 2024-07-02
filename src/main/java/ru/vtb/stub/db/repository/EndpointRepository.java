package ru.vtb.stub.db.repository;

import io.micronaut.data.mongodb.annotation.MongoAggregateQuery;
import io.micronaut.data.mongodb.annotation.MongoDeleteQuery;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import ru.vtb.stub.db.entity.EndpointEntity;

import java.util.List;
import java.util.Optional;

@MongoRepository
public interface EndpointRepository extends CrudRepository<EndpointEntity, String> {

    @MongoAggregateQuery(value = ""
            + "[{ "
            + "        $match: { "
            + "            $and: [{ "
            + "                $expr: { "
            + "                    $regexMatch: { "
            + "                        input: :path, "
            + "                        regex: {$concat: [ "
            + "                            {$toString: '^'}, "
            + "                            {$getField: { "
            + "                                input: { "
            + "                                    $getField: {input: '$$CURRENT', field: 'second_id'} "
            + "                                }, "
            + "                                field: 'path' "
            + "                            } "
            + "                            }] "
            + "                        }, "
            + "                        options:'i' "
            + "                    } "
            + "                } "
            + "            }, "
            + "            {'second_id.team': :team}, "
            + "            {'second_id.method': :method} "
            + "            ] "
            + "        } "
            + "}]")
    Optional<EndpointEntity> findByTeamAndNormalPathAndMethod(String team, String path, String method);

    @MongoFindQuery(value = "{ "
            + "  $and: [ "
            + "    { "
            + "      $or: [ "
            + "        { "
            + "          'second_id.path': { "
            + "            $regex: :path "
            + "          } "
            + "        }, "
            + "        { "
            + "          $and: [ "
            + "            { "
            + "              'second_id.path': :path "
            + "            }, "
            + "            { "
            + "              is_regex: true "
            + "            } "
            + "          ] "
            + "        } "
            + "      ] "
            + "    }, "
            + "    { "
            + "      'second_id.team': :team "
            + "    }, "
            + "    { "
            + "      'second_id.method': :method "
            + "    } "
            + "  ] "
            + "}")
    List<EndpointEntity> findByTeamAndRegexPathAndMethod(String team, String path, String method);

    @MongoDeleteQuery(value = "{ "
            + "  $and: [ "
            + "    { "
            + "      $or: [ "
            + "        { "
            + "          'second_id.path': { "
            + "            $regex: :path "
            + "          } "
            + "        }, "
            + "        { "
            + "          $and: [ "
            + "            { "
            + "              'second_id.path': :path "
            + "            }, "
            + "            { "
            + "              is_regex: true "
            + "            } "
            + "          ] "
            + "        } "
            + "      ] "
            + "    }, "
            + "    { "
            + "      'second_id.team': :team "
            + "    }, "
            + "    { "
            + "      'second_id.method': :method "
            + "    } "
            + "  ] "
            + "}")
    void deleteByTeamAndRegexPathAndMethod(String team, String path, String method);

    @MongoFindQuery(value = "{ "
            + "  'second_id.team': :team "
            + "}")
    List<EndpointEntity> findTop30ByTeam(String team);

    @MongoFindQuery(value = "{'second_id.team': :team}")
    Optional<EndpointEntity> findTop1ByTeam(String team);

    @MongoDeleteQuery(value = "{'second_id.team': :team}")
    void deleteByTeam(String team);

}
