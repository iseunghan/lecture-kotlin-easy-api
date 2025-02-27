package me.iseunghan.lectureeasykopringapi.blog.repository

import me.iseunghan.lectureeasykopringapi.blog.entity.WordCount
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WordRepository: CrudRepository<WordCount, String>