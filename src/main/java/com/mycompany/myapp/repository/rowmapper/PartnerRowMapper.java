package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Partner;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Partner}, with proper type conversions.
 */
@Service
public class PartnerRowMapper implements BiFunction<Row, String, Partner> {

    private final ColumnConverter converter;

    public PartnerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Partner} stored in the database.
     */
    @Override
    public Partner apply(Row row, String prefix) {
        Partner entity = new Partner();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodep(converter.fromRow(row, prefix + "_codep", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setContact(converter.fromRow(row, prefix + "_contact", String.class));
        entity.setLogoContentType(converter.fromRow(row, prefix + "_logo_content_type", String.class));
        entity.setLogo(converter.fromRow(row, prefix + "_logo", byte[].class));
        entity.setIconContentType(converter.fromRow(row, prefix + "_icon_content_type", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", byte[].class));
        return entity;
    }
}
