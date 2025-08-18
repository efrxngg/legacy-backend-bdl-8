package com.bancodellitoral.demo.repository;

import com.bancodellitoral.demo.config.database.pool.PostgreConnectionPool;
import com.bancodellitoral.demo.dto.RSCBC;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class PSRepository {

    @Inject
    private PostgreConnectionPool pool;

    public List<RSCBC> bigSelect() {
        List<RSCBC> listaReportes = new LinkedList<>();
        String sql = "SELECT t.* FROM public.reporte_saldo_cap_bpr_cnodo t";

        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RSCBC dto = new RSCBC();
                dto.setId(resultSet.getString("id"));
                dto.setIdBodega(resultSet.getString("id_bodega"));

                listaReportes.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaReportes;
    }

    public List<RSCBC> searchPaginatedByCursor(Integer cursor) {
        List<RSCBC> listaReportes = new LinkedList<>();
        String sql = "SELECT t.* FROM public.reporte_saldo_cap_bpr_cnodo t " +
                "where t.id > ? order by t.id limit 1000";

        try (Connection connection = pool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cursor);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RSCBC dto = new RSCBC();
                dto.setId(resultSet.getString("id"));
                    dto.setIdBodega(resultSet.getString("id_bodega"));

                listaReportes.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaReportes;
    }

}
