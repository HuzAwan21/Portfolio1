// ===== PORTFOLIO DATA SAMPLES & EXAMPLES =====
// This file shows examples of populated data. Copy sections to data.js as needed.

// EXAMPLE 1: Fully populated portfolio
const examplePortfolioData = {
    name: "Jane Developer",
    title: "Full Stack Developer | AI Researcher | Tech Speaker",
    bio: "Passionate about building user-centric applications and advancing AI research. With 5+ years of experience in web development and 2 years in machine learning, I love solving complex problems and sharing knowledge through writing and speaking.",
    profileImage: "https://avatars.githubusercontent.com/u/12345?s=400",
    cvUrl: "https://drive.google.com/uc?export=download&id=FILE_ID",
    
    skills: [
        "JavaScript/TypeScript",
        "Python",
        "React.js",
        "Node.js",
        "TensorFlow",
        "MongoDB",
        "AWS",
        "Docker",
        "PostgreSQL",
        "GraphQL",
        "Vue.js",
        "REST APIs",
        "ML/AI",
        "Leadership"
    ],
    
    projects: [
        {
            id: 1,
            title: "AI-Powered E-Commerce Platform",
            description: "Full-stack platform with ML-based product recommendations and real-time inventory management",
            category: "web",
            image: "🛍️",
            tags: ["React", "Node.js", "MongoDB", "TensorFlow", "AWS"],
            link: "https://github.com/janedev/ecommerce-ai",
            details: "Developed a complete e-commerce solution serving 50,000+ monthly users. Implemented a collaborative filtering recommendation engine that increased average order value by 35%. Features include real-time inventory sync, payment processing with Stripe, and an admin dashboard for analytics. Tech stack: React, Express, MongoDB, Redis for caching, and TensorFlow for recommendations. Deployed on AWS with CI/CD pipeline using GitHub Actions."
        },
        {
            id: 2,
            title: "Real-Time Collaboration Tool",
            description: "Live document editing platform with video conferencing and AI-powered summaries",
            category: "web",
            image: "📝",
            tags: ["Vue.js", "WebSocket", "Node.js", "PostgreSQL"],
            link: "https://github.com/janedev/collab-tool",
            details: "Built a Google Docs-like application with real-time collaboration using WebSockets. Features include simultaneous multi-user editing with conflict resolution, integrated video conferencing using WebRTC, and AI-powered document summarization. Supports up to 100 concurrent users per document. Reduced document version conflicts by 98% with custom OT algorithm."
        },
        {
            id: 3,
            title: "Mental Health Mobile App",
            description: "Cross-platform app connecting users with mental health resources and peer support",
            category: "mobile",
            image: "💚",
            tags: ["React Native", "Firebase", "Python"],
            link: "https://github.com/janedev/mental-health-app",
            details: "Developed a React Native app available on iOS and Android with 10,000+ downloads. Features include resource directory, peer support groups, therapy provider matching, and crisis hotline integration. Implemented backend with Firebase for real-time data sync. Added NLP features to identify crisis keywords and route users to appropriate help. Privacy-first design with end-to-end encryption."
        },
        {
            id: 4,
            title: "Sentiment Analysis Engine",
            description: "NLP model for analyzing sentiment in customer feedback with multi-language support",
            category: "ai",
            image: "🤖",
            tags: ["Python", "TensorFlow", "BERT", "Flask"],
            link: "https://github.com/janedev/sentiment-engine",
            details: "Built a transformer-based sentiment analysis model using BERT, fine-tuned on 100,000+ labeled reviews. Achieves 94% accuracy across English, Spanish, and French. Deployed as REST API serving 1000+ requests/day. Integrated with client platforms for real-time feedback analysis. Model trained on Azure ML Platform with automated retraining pipeline."
        },
        {
            id: 5,
            title: "Portfolio Website",
            description: "Modern, responsive portfolio site with dark mode and interactive project filtering",
            category: "web",
            image: "💼",
            tags: ["HTML", "CSS", "JavaScript", "Responsive"],
            link: "https://github.com/janedev/portfolio",
            details: "Created this portfolio site from scratch using vanilla JS and CSS animations. Features smooth scrolling, project filtering by category, modal project details, and contact form integration. Fully responsive design optimized for mobile. Lighthouse scores: Accessibility 100, Best Practices 100, Performance 98."
        }
    ],
    
    research: [
        {
            id: 1,
            title: "Evaluating Transformer Models for Low-Resource Languages",
            date: "2024",
            description: "A comprehensive study of fine-tuning strategies for BERT models in underrepresented languages, achieving state-of-the-art results in multiple NLP tasks.",
            url: "https://arxiv.org/abs/2024.12345",
            abstractShort: "We present a novel fine-tuning methodology for transformer models applied to low-resource language tasks. Our approach combines transfer learning with data augmentation techniques, achieving 12% improvement over baseline models on NER and sentiment analysis tasks."
        },
        {
            id: 2,
            title: "Efficient Attention Mechanisms for Mobile ML",
            date: "2023",
            description: "Exploring lightweight attention mechanisms suitable for on-device machine learning, reducing model size by 40% without significant accuracy loss.",
            url: "https://arxiv.org/abs/2023.54321",
            abstractShort: "This paper investigates efficient attention mechanisms optimized for mobile and edge devices. We propose DistilAttention, a novel approach that reduces computational complexity by 60% while maintaining 98% of the original model's accuracy on standard benchmarks."
        },
        {
            id: 3,
            title: "Fairness in Recommendation Systems: A Case Study",
            date: "2023",
            description: "Analysis of bias in collaborative filtering algorithms and proposed mitigation strategies",
            url: "https://researchgate.net/publication/123456789",
            abstractShort: "We analyze bias patterns in recommendation systems and propose a fairness-aware methodology. Our evaluation shows 35% reduction in gender bias while maintaining recommendation quality."
        }
    ],
    
    presentations: [
        {
            id: 1,
            title: "Building AI Products That Users Actually Want",
            date: "October 2024",
            event: "AI Summit 2024",
            description: "A talk about bridging the gap between AI research and practical product implementation, with lessons learned from 5 years of AI product development.",
            url: "https://slides.com/janedev/ai-products",
            icon: "🤖"
        },
        {
            id: 2,
            title: "Scaling Machine Learning in Production",
            date: "August 2024",
            event: "PyData Conference 2024",
            description: "Deep dive into MLOps best practices, model serving strategies, and monitoring ML systems in production environments.",
            url: "https://slides.com/janedev/ml-scaling",
            icon: "📊"
        },
        {
            id: 3,
            title: "The Future of Low-Code Development",
            date: "June 2024",
            event: "Web Dev Summit 2024",
            description: "Exploring emerging low-code platforms and their impact on software development velocity and accessibility.",
            url: "https://slides.com/janedev/lowcode-future",
            icon: "💻"
        },
        {
            id: 4,
            title: "Ethics in AI: Building Responsible Systems",
            date: "April 2024",
            event: "Tech Leaders Forum",
            description: "Panel discussion on ethical considerations in AI development, bias mitigation, and responsible AI practices.",
            url: "#",
            icon: "⚖️"
        }
    ],
    
    gallery: [
        {
            id: 1,
            title: "Speaking at AI Summit 2024",
            icon: "📸",
            url: "https://via.placeholder.com/400x300?text=Speaking"
        },
        {
            id: 2,
            title: "Hackathon Winner - Nov 2023",
            icon: "🏆",
            url: "https://via.placeholder.com/400x300?text=Hackathon"
        },
        {
            id: 3,
            title: "Conference Booth Demo",
            icon: "🎪",
            url: "https://via.placeholder.com/400x300?text=Conference"
        },
        {
            id: 4,
            title: "Team Photo at Company Retreat",
            icon: "👥",
            url: "https://via.placeholder.com/400x300?text=Team"
        },
        {
            id: 5,
            title: "Product Launch Event",
            icon: "🚀",
            url: "https://via.placeholder.com/400x300?text=Launch"
        },
        {
            id: 6,
            title: "Mentoring Bootcamp Students",
            icon: "👨‍🏫",
            url: "https://via.placeholder.com/400x300?text=Mentoring"
        }
    ],
    
    experience: [
        {
            id: 1,
            position: "Senior Full Stack Engineer",
            company: "Tech Corp International",
            duration: "2022 - Present",
            description: "Leading development of machine learning-powered web platform serving 100k+ users. Managing team of 3 engineers and collaborating with data scientists on ML pipeline integration.",
            highlights: [
                "Architected microservices infrastructure supporting 5M+ daily requests",
                "Reduced API response time by 60% through caching and optimization",
                "Implemented CI/CD pipeline reducing deployment time from 2 hours to 10 minutes",
                "Mentored 3 junior developers who were promoted to mid-level engineers",
                "Led technical interviews and hiring for engineering team expansion"
            ]
        },
        {
            id: 2,
            position: "Full Stack Developer",
            company: "StartUp Innovations",
            duration: "2020 - 2022",
            description: "Built and maintained multiple web applications from concept to production, working closely with product and design teams in an agile environment.",
            highlights: [
                "Developed real-time collaboration features using WebSockets serving 1000+ concurrent users",
                "Increased application performance by 40% through React optimization and code splitting",
                "Implemented complex database queries and indexing strategies reducing query time by 70%",
                "Contributed to open-source projects with 500+ GitHub stars",
                "Established coding standards and best practices for the engineering team"
            ]
        },
        {
            id: 3,
            position: "Junior Web Developer",
            company: "Digital Agency Pro",
            duration: "2019 - 2020",
            description: "Developed responsive websites and web applications for various clients, learning full-stack development from experienced mentors.",
            highlights: [
                "Built 15+ responsive websites using React and Node.js",
                "Fixed critical bugs in production reducing downtime by 90%",
                "Learned modern web development practices and best practices",
                "Improved CSS and accessibility knowledge leading to better UX scores",
                "Completed multiple client projects ahead of schedule"
            ]
        }
    ],
    
    education: [
        {
            id: 1,
            degree: "Master of Science",
            field: "Computer Science - Artificial Intelligence Track",
            institution: "Tech State University",
            year: "2019",
            details: "Focus on Machine Learning, Natural Language Processing, and Neural Networks. Thesis: 'Efficient Attention Mechanisms for Mobile ML'. GPA: 3.9/4.0. Dean's List all semesters."
        },
        {
            id: 2,
            degree: "Bachelor of Science",
            field: "Computer Science",
            institution: "State University",
            year: "2017",
            details: "Minor in Mathematics. Completed coursework in Data Structures, Algorithms, Databases, and Software Engineering. GPA: 3.8/4.0"
        },
        {
            id: 3,
            degree: "Professional Certificate",
            field: "Machine Learning Specialization",
            institution: "Coursera (Andrew Ng)",
            year: "2020",
            details: "Completed specialized track covering supervised learning, unsupervised learning, neural networks, and practical ML projects."
        },
        {
            id: 4,
            degree: "AWS Certified Solutions Architect",
            field: "Cloud Architecture Certification",
            institution: "Amazon Web Services",
            year: "2022",
            details: "Professional level certification validating AWS cloud architecture expertise and best practices."
        }
    ],
    
    contact: {
        email: "jane@example.com",
        phone: "+1 (555) 987-6543",
        location: "San Francisco, California",
        linkedIn: "https://linkedin.com/in/janedev",
        github: "https://github.com/janedev",
        twitter: "https://twitter.com/janedev",
        portfolio: "https://janedev.com"
    },
    
    social: [
        { name: "LinkedIn", url: "https://linkedin.com/in/janedev", icon: "fab fa-linkedin" },
        { name: "GitHub", url: "https://github.com/janedev", icon: "fab fa-github" },
        { name: "Twitter", url: "https://twitter.com/janedev", icon: "fab fa-twitter" },
        { name: "Email", url: "mailto:jane@example.com", icon: "fas fa-envelope" },
        { name: "Medium", url: "https://medium.com/@janedev", icon: "fab fa-medium" },
        { name: "Dev.to", url: "https://dev.to/janedev", icon: "fab fa-dev" }
    ]
};

// ===== EXAMPLE 2: Minimal Portfolio (quick start) =====
const minimalPortfolioData = {
    name: "Your Name",
    title: "Your Title",
    bio: "Write a brief bio about yourself.",
    profileImage: "https://via.placeholder.com/300?text=Your+Photo",
    cvUrl: "#",
    skills: ["Skill 1", "Skill 2", "Skill 3", "Skill 4"],
    projects: [
        {
            id: 1,
            title: "Project Name",
            description: "What this project does",
            category: "web",
            image: "📱",
            tags: ["Tech1", "Tech2"],
            link: "https://github.com",
            details: "Full details about your project"
        }
    ],
    research: [],
    presentations: [],
    gallery: [],
    experience: [],
    education: [],
    contact: {
        email: "your.email@example.com",
        phone: "",
        location: "City, Country"
    },
    social: [
        { name: "GitHub", url: "https://github.com", icon: "fab fa-github" },
        { name: "Email", url: "mailto:your.email@example.com", icon: "fas fa-envelope" }
    ]
};

// Copy these snippets to customize your portfolio!
// Remember to use these as templates in your main data.js file
