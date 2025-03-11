package com.example.apicontactapp.service;

import com.example.apicontactapp.domain.Contact;
import com.example.apicontactapp.repo.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.apicontactapp.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<Contact> getAllContacts(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContact(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(Contact contact) {
        //todo
    }

    public String updatePhoto(String id, MultipartFile photoFile) {
        log.info("UPDATING PHOTO FOR USER");
        Contact contact = getContact(id);
        String photoUrl = photoFunction.apply(id, photoFile);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains(".")).map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".jpg");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        try {
            Path pathStorageFile = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(pathStorageFile)) {
                Files.createDirectories(pathStorageFile);
            }
            Files.copy(image.getInputStream(), pathStorageFile.resolve(id + fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save image");
        }
    };
}
