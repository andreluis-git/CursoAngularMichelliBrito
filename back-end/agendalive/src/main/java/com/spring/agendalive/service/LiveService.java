package com.spring.agendalive.service;

import com.spring.agendalive.document.LiveDocument;
import com.spring.agendalive.repository.LiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LiveService {

    @Autowired
    LiveRepository liveRepository;

    public Page<LiveDocument> findAll(Pageable pageable, String flag){
        if(flag != null && flag.equals("next")){
            return liveRepository.findByLiveDateAfterOrderByLiveDateAsc(LocalDateTime.now(), pageable);
        }else if(flag != null && flag.equals("previous")){
            return liveRepository.findByLiveDateBeforeOrderByLiveDateDesc(LocalDateTime.now(), pageable);
        }else{
            return liveRepository.findAll(pageable);
        }
    }

    public Optional<LiveDocument> findById(String id){
        return liveRepository.findById(id);
    }

    public LiveDocument save(LiveDocument liveDocument){
        if (liveDocument.getLiveLink().contains("youtu")) {
            String link = liveDocument.getLiveLink();
            link = link.replace("watch?v=", "embed/");
            link = link.replace("&list", "?list");
            String[] linkSplit = link.split("&");
            int i = 1;
            while (linkSplit[linkSplit.length - i].contains("index") || linkSplit[linkSplit.length - i].contains("channel")){
                linkSplit[linkSplit.length - i] = "";
                i++;
            }
            StringBuilder remontaLink = new StringBuilder();
            for (String item : linkSplit) {
                remontaLink.append(item);
            }
            link = remontaLink.toString();
            liveDocument.setLiveLink(link);
        }
        return liveRepository.save(liveDocument);
    }

    public void delete(LiveDocument liveDocument){
        liveRepository.delete(liveDocument);
    }
}
