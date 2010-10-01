/*
 * Copyright 2010 Mike Novak <michael.novakjr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudbits.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class FeedContract {

    public static final long UPDATED_NEVER = -2;
    public static final long UPDATED_UNKNOWN = -1;

    public interface SyncColumns {
        String UPDATED = "updated";
    }

    interface FeedsColumns {
        String FEED_ID = "feed_id";
        String FEED_TITLE = "feed_title";
        String FEED_URL = "feed_url";
        String FEED_ICON = "feed_icon";
        String FEED_SUBSCRIBED = "feed_subscribed";
    }

    interface PostsColumns {
        String POST_ID = "post_id";
        String POST_TITLE = "post_title";
        String POST_AUTHOR = "post_author";
        String POST_DATE = "post_date";
        String POST_SUMMARY = "post_summary";
        String POST_URL = "post_url";
        String POST_STARRED = "post_starred";
        String POST_LIKES = "post_likes";
        String POST_SHARED = "post_shared";
    }

    public static final String CONTENT_AUTHORITY = "org.cloudbits";
    
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_FEEDS = "feeds";
    private static final String PATH_POSTS = "posts";

    public static class Feeds implements FeedsColumns, BaseColumns {
        public static final Uri CONTENT_URI = 
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FEEDS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cloudbits.feed";
        public static final String CONTENT_ITEM_TYPE = 
            "vnd.android.cursor.item/vnd.cloudbits.feed";

        public static Uri buildFeedUri(String feedId) {
            return CONTENT_URI.buildUpon().appendPath(feedId).build();
        }

        public static String getFeedId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Posts implements PostsColumns, BaseColumns {
        public static final Uri CONTENT_URI = 
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cloudbits.post";
        public static final String CONTENT_ITEM_TYPE = 
            "vnd.android.cursor.item/vnd.cloudbits.post";

        public static final Uri buildPostUri(String postId) {
            return CONTENT_URI.buildUpon().appendPath(postId).build();
        }

        public static String getPostId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private FeedContract() {}
}

