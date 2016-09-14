// Databricks notebook source exported at Wed, 14 Sep 2016 10:36:36 UTC
// MAGIC %md
// MAGIC # Sentiments of Tweets from first GOP Debate 2015
// MAGIC 
// MAGIC ### 2016 Raazesh Sainudiin

// COMMAND ----------

// MAGIC %md
// MAGIC `Sentiment.csv` was unzipped from `first-gop-debate-twitter-sentiment-release-2015-12-28-05-51-00.zip` that was
// MAGIC downloaded from [kaggle](https://www.kaggle.com/crowdflower/first-gop-debate-twitter-sentiment).
// MAGIC 
// MAGIC ## Description
// MAGIC 
// MAGIC This data originally came from [Crowdflower's Data for Everyone library](http://www.crowdflower.com/data-for-everyone)
// MAGIC 
// MAGIC As the original source says,
// MAGIC 
// MAGIC         We looked through tens of thousands of tweets about the early August GOP debate in Ohio
// MAGIC         and asked contributors to do both sentiment analysis and data categorization.
// MAGIC         Contributors were asked if the tweet was relevant, which candidate was mentioned,
// MAGIC         what subject was mentioned, and then what the sentiment was for a given tweet.
// MAGIC         We've removed the non-relevant messages from the uploaded dataset.
// MAGIC 
// MAGIC The data we're providing on Kaggle is a slightly reformatted version of the original source. It includes both a CSV file and SQLite database. The code that does these transformations is available on GitHub
// MAGIC Released Under [CC BY-NC-SA 4.0 License](https://creativecommons.org/licenses/by-nc-sa/4.0/).
// MAGIC 
// MAGIC ```
// MAGIC $ head Sentiment.csv
// MAGIC id,candidate,candidate_confidence,relevant_yn,relevant_yn_confidence,sentiment,sentiment_confidence,subject_matter,subject_matter_confidence,candidate_gold,name,relevant_yn_gold,retweet_count,sentiment_gold,subject_matter_gold,text,tweet_coord,tweet_created,tweet_id,tweet_location,user_timezone
// MAGIC 1,No candidate mentioned,1.0,yes,1.0,Neutral,0.6578,None of the above,1.0,,I_Am_Kenzi,,5,,,RT @NancyLeeGrahn: How did everyone feel about the Climate Change question last night? Exactly. #GOPDebate,,2015-08-07 09:54:46 -0700,629697200650592256,,Quito
// MAGIC 2,Scott Walker,1.0,yes,1.0,Positive,0.6333,None of the above,1.0,,PeacefulQuest,,26,,,RT @ScottWalker: Didn't catch the full #GOPdebate last night. Here are some of Scott's best lines in 90 seconds. #Walker16 http://t.co/ZSfF…,,2015-08-07 09:54:46 -0700,629697199560069120,,
// MAGIC 3,No candidate mentioned,1.0,yes,1.0,Neutral,0.6629,None of the above,0.6629,,PussssyCroook,,27,,,RT @TJMShow: No mention of Tamir Rice and the #GOPDebate was held in Cleveland? Wow.,,2015-08-07 09:54:46 -0700,629697199312482304,,
// MAGIC 4,No candidate mentioned,1.0,yes,1.0,Positive,1.0,None of the above,0.7039,,MattFromTexas31,,138,,,RT @RobGeorge: That Carly Fiorina is trending -- hours after HER debate -- above any of the men in just-completed #GOPdebate says she's on …,,2015-08-07 09:54:45 -0700,629697197118861312,Texas,Central Time (US & Canada)
// MAGIC 5,Donald Trump,1.0,yes,1.0,Positive,0.7045,None of the above,1.0,,sharonDay5,,156,,,RT @DanScavino: #GOPDebate w/ @realDonaldTrump delivered the highest ratings in the history of presidential debates. #Trump2016 http://t.co…,,2015-08-07 09:54:45 -0700,629697196967903232,,Arizona
// MAGIC 6,Ted Cruz,0.6332,yes,1.0,Positive,0.6332,None of the above,1.0,,DRJohnson11,,228,,,"RT @GregAbbott_TX: @TedCruz: ""On my first day I will rescind every illegal executive action taken by Barack Obama."" #GOPDebate @FoxNews",,2015-08-07 09:54:44 -0700,629697194283499520,,Central Time (US & Canada)
// MAGIC 7,No candidate mentioned,1.0,yes,1.0,Negative,0.6761,FOX News or Moderators,1.0,,DebWilliams57,,17,,,RT @warriorwoman91: I liked her and was happy when I heard she was going to be the moderator. Not anymore. #GOPDebate @megynkelly  https://…,,2015-08-07 09:54:44 -0700,629697192383672320,North Georgia,Eastern Time (US & Canada)
// MAGIC 8,No candidate mentioned,1.0,yes,1.0,Neutral,1.0,None of the above,1.0,,RaulAReyes,,0,,,Going on #MSNBC Live with @ThomasARoberts around 2 PM ET.  #GOPDebate,,2015-08-07 09:54:44 -0700,629697192169750528,New York NY,Eastern Time (US & Canada)
// MAGIC 9,Ben Carson,1.0,yes,1.0,Negative,0.6889,None of the above,0.6444,,kengpdx,,0,,,"Deer in the headlights RT @lizzwinstead: Ben Carson, may be the only brain surgeon who has performed a lobotomy on himself. #GOPDebate",,2015-08-07 09:54:44 -0700,629697190219243524,,Pacific Time (US & Canada)
// MAGIC ```

// COMMAND ----------

// Load the raw dataset stored as a CSV file
val s = sqlContext.
    read.
    format("com.databricks.spark.csv").
    options(Map("header" -> "true", "delimiter" -> ",", "mode" -> "PERMISSIVE", "inferSchema" -> "true")).
    load("dbfs:////datasets/gop/2015/Sentiment.csv")
  

// COMMAND ----------

display(s)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Downloading and Loading Data into DBFS

// COMMAND ----------

// MAGIC %sh
// MAGIC wget https://github.com/raazesh-sainudiin/scalable-data-science/raw/master/meme-evolution/datasets/first-gop-debate-2015/Sentiment.csv.tgz

// COMMAND ----------

// MAGIC %sh
// MAGIC ls && pwd

// COMMAND ----------

// MAGIC %sh
// MAGIC tar zxvf Sentiment.csv.tgz

// COMMAND ----------

// MAGIC %sh
// MAGIC rm Sentiment.csv.tgz && ls

// COMMAND ----------

dbutils.fs.mkdirs("/datasets/gop/2015/")

// COMMAND ----------

dbutils.fs.mv("file:///databricks/driver/Sentiment.csv","dbfs:///datasets/gop/2015/")

// COMMAND ----------

display(dbutils.fs.ls("/datasets/gop/2015"))

// COMMAND ----------

val s = sc.textFile("/datasets/gop/2015/Sentiment.csv")

// COMMAND ----------

s.take(2)

// COMMAND ----------

