package com.mycompany.test2.db;

import com.ibm.icu.text.Transliterator;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SlugListener {
    @PrePersist
    @PreUpdate
    public void saveDictServiceType(DictServiceType dictServiceType) {
        dictServiceType.setSlug(normalize(dictServiceType.getDisplayName()));
        log.info("Attempting DictServiceType: {}", dictServiceType);
    }

    // @PrePersist
    // @PreUpdate
    // public void saveDictApplicationPlatform(DictApplicationPlatform dictApplicationPlatform) {
    //     dictApplicationPlatform.setSlug(normalize(dictApplicationPlatform.getName()));
    //     log.info("Attempting DictApplicationPlatform: {}", dictApplicationPlatform);
    // }

    public final static String normalize(String value) {
        if (value == null) {
            return null;
        }
        var transliterator = Transliterator.getInstance("Any-Latin; NFKD; Latin-ASCII; Lower; [^\\p{Alnum}] Remove;");
        var slug = transliterator.transliterate(value);
        log.info("slug '{}' -> '{}'", value, slug);
        return slug;
    }
}
