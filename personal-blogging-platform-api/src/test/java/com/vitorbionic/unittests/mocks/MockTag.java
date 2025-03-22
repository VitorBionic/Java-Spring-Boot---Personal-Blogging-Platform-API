package com.vitorbionic.unittests.mocks;

import java.util.ArrayList;
import java.util.List;

import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.TagDTO;

public class MockTag {
    
    private Long idCountEnt = 0L;
    private Long idCountDto = 0L;
    
    public Tag mockEntity() {
        return new Tag(idCountEnt++, "Description " + idCountEnt);
    }
    
    public TagDTO mockDTO() {
        return new TagDTO(idCountDto++, "Description " + idCountDto);
    }
    
    public List<Tag> mockEntityList() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 14; i++)
            tags.add(mockEntity());
        return tags;
    }
    
    public List<TagDTO> mockDTOList() {
        List<TagDTO> tags = new ArrayList<>();
        for (int i = 0; i < 14; i++)
            tags.add(mockDTO());
        return tags;
    }
    
    public Tag mockEntity(Long id) {
        return new Tag(id, "Description " + (id + 1));
    }
    
    public TagDTO mockDTO(Long id) {
        return new TagDTO(id, "Description " + (id + 1));
    }
    
}
