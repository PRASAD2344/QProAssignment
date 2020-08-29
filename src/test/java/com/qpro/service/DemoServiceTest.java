package com.qpro.service;

import com.qpro.bo.AppUser;
import com.qpro.bo.Item;
import com.qpro.bo.User;
import com.qpro.repository.AppUserRepository;
import com.qpro.repository.ItemRepository;
import com.qpro.repository.UserRepository;
import org.junit.Test;
import org.mockito.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoServiceTest {

    @InjectMocks
    DemoService demoService;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Captor
    ArgumentCaptor argCaptor;

    @Test
    public void givenAddress_whenItIsNotNull_thenGetBestStories() {
        String remoteAddress = "127.0.0.1";

        //When saving app user, capture the argument.
        when(appUserRepository.containsKey(remoteAddress)).thenReturn(false);
        doNothing().when(appUserRepository).save((AppUser) argCaptor.capture());

        //Get dummy object as item
        when(itemRepository.containsKey(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(new Item());

        List<Item> returnItems = demoService.bestStories(remoteAddress);
        AppUser storedUser = (AppUser) argCaptor.getValue();
        assertEquals(returnItems.size(),storedUser.getTopItemsServed().size());
        assertEquals(remoteAddress, storedUser.getRemoteAddress());
    }

    @Test
    public void givenNewAddress_whenItIsNotNull_thenGetPastStories() {
        String remoteAddress = "127.0.0.1";

        //When saving app user, capture the argument.
        when(appUserRepository.containsKey(remoteAddress)).thenReturn(false);
        doNothing().when(appUserRepository).save(any());

        List<Item> returnItems = demoService.pastStories(remoteAddress);
        //Expect size to be zero, as this is new user
        assertEquals(returnItems.size(),0);
    }

    @Test
    public void givenAddress_whenItIsNotNull_thenGetPastStories() {
        String remoteAddress = "127.0.0.1";

        //When saving app user, capture the argument.
        when(appUserRepository.containsKey(remoteAddress)).thenReturn(true);
        when(appUserRepository.findById(any())).thenReturn(new AppUser(
                remoteAddress,
                new HashSet(Arrays.asList(10L))
        ));

        //Get dummy object as item
        Item sampleStoryItem = new Item();
        sampleStoryItem.setType("story");
        when(itemRepository.containsKey(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(sampleStoryItem);

        List<Item> returnItems = demoService.pastStories(remoteAddress);
        assertEquals(returnItems.size(),1);
    }

    @Test
    public void givenStory_withNoComments_checkIfWeGetEmptyList(){
        //Get dummy object as item
        Item sampleStoryItem = new Item();
        sampleStoryItem.setType("story");
        when(itemRepository.containsKey(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(sampleStoryItem);

        List<Item> returnItems = demoService.comments(1L);
        assertEquals(returnItems.size(),0);
    }

    @Test
    public void givenStory_withOneComment_checkIfWeNonEmptyList(){
        //Get dummy object as item
        Item sampleStoryItem = new Item();
        sampleStoryItem.setType("story");
        sampleStoryItem.getKids().add(2L);//Adding a comment to story
        when(itemRepository.containsKey(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(sampleStoryItem);

        List<Item> returnItems = demoService.comments(1L);
        assertEquals(returnItems.size(),1);
    }

    @Test
    public void givenUser_presentInCache_checkIfWeAreAbleToRetrieveIt(){
        User dummyUser = new User();
        dummyUser.setId("rmason");
        when(userRepository.containsKey(anyString())).thenReturn(true);
        when(userRepository.findById(anyString())).thenReturn(dummyUser);

        User returnItem = demoService.getUser("rmason");
        assertEquals(returnItem.getId(),"rmason");
    }

    @Test
    public void givenUser_notPresentInCache_checkIfWeAreAbleToRetrieveIt(){
        when(userRepository.containsKey(anyString())).thenReturn(false);
        doNothing().when(userRepository).save(any());

        User returnItem = demoService.getUser("rmason");
        assertEquals(returnItem.getId(),"rmason");
    }
}
